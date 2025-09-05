import "../css/Trading.css";
import { useState, useEffect, useRef } from "react";
import axios from 'axios'

function Trading() {
  const [data, setData] = useState([]);

  const [usd, setUsd] = useState("");
  const [running, setRunning] = useState(false);
  const [started, setStarted] = useState(false);
  const intervalRef = useRef(null);
  const indexRef = useRef(0);

  useEffect(() => {
    axios.get('http://localhost:8080/trading')
      .then(res => setData(res.data))
      .catch(err => console.error('Error fetching crypto information:', err));
  }, []);

  const start = () => {
    if (!running && !started) {
      setUsd("25000");
      setStarted(true);
      setRunning(true);
      intervalRef.current = setInterval(() => {}, 60000);
    }
  };

  const pause = () => {
    if (running && started) {
      setRunning(false);
      clearInterval(intervalRef.current);
    }
    else if (!running && started) {
      setRunning(true);
      intervalRef.current = setInterval(() => {}, 60000);
    }
  };

  const reset = () => {
    setStarted(false);
    setRunning(false);
    clearInterval(intervalRef.current);
    setUsd("25000");
    indexRef.current = 0;
  };

  useEffect(() => {
    return () => clearInterval(intervalRef.current);
  }, []);

  const totalCrypto = data.reduce((sum, e) => sum + parseFloat(e.total), 0);
  const totalRevenue = totalCrypto + parseFloat(usd || 0);

  return (
    <div className="trading-container">
      <h1 className="trading-title">Trading</h1>

      <div className="controls">
        <button className="start" onClick={start}>Start</button>
        <button className="pause" onClick={pause}>Pause</button>
        <button className="reset" onClick={reset}>Reset</button>
      </div>

      <div className="usd-label">USD: ${usd}</div>

      <table className="trading-table">
        <thead>
          <tr>
            <th>Crypto</th>
            <th>Quantity</th>
            <th>Price (USD)</th>
            <th>Total (USD)</th>
          </tr>
        </thead>
        <tbody>
          {data.map((entry, index) => (
            <tr key={index}>
              <td>{entry.currency}</td>
              <td>{entry.amount}</td>
              <td>{Number(entry.price).toFixed(2)}</td>
              <td>{Number(entry.amount * entry.price).toFixed(2)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="revenue-label">Total Revenue: ${totalRevenue}</div>
    </div>
  );
}

export default Trading;
