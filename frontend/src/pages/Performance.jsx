import '../css/Performance.css'
import { useState, useEffect } from 'react'
import axios from 'axios'

function Performance() {
  const [data, setData] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/performance')
      .then(res => setData(res.data))
      .catch(err => console.error('Error fetching performance:', err));
  }, []);

  return (
    <div className="performance-container">
      <h1 className="performance-title">Performance</h1>
      <table className="performance-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Revenue</th>
          </tr>
        </thead>
        <tbody>
          {data.map((entry, index) => (
            <tr key={index}>
              <td>{new Date(entry.checkedAt).toLocaleDateString()}</td>
              <td className={entry.revenue >= 0 ? "revenue-positive" : "revenue-negative"}>
                ${Number(entry.revenue).toFixed(2)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Performance;