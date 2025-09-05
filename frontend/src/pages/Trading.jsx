import "../css/Trading.css";
import { useState, useEffect, useRef } from "react";

function Trading() {
  const [usd, setUsd] = useState("");
  const [running, setRunning] = useState(false);
  const [started, setStarted] = useState(false);
  const intervalRef = useRef(null);
  const indexRef = useRef(0);

  const data = [
    { crypto: "BTC", quantity: 10, price: 50, total: 500 },
    { crypto: "ETH", quantity: 5, price: 70, total: 350 },
    { crypto: "ADA", quantity: 8, price: 40, total: 320 },
    { crypto: "XRP", quantity: 8, price: 60, total: 480 },
    { crypto: "DOGE", quantity: 3, price: 30, total: 90 },
  ];

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
              <td>{entry.crypto}</td>
              <td>{entry.quantity}</td>
              <td>{entry.price}</td>
              <td>{entry.total}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="revenue-label">Total Revenue: ${totalRevenue}</div>
    </div>
  );
}

export default Trading;
