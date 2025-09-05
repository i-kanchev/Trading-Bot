import '../css/Transactions.css'
import { useState, useEffect } from 'react'
import axios from 'axios'

function Transactions() {
  const [data, setData] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/transactions')
      .then(res => setData(res.data))
      .catch(err => console.error('Error fetching transactions:', err));
  }, []);

  return (
    <div className="transactions-container">
      <h1 className="transactions-title">Transactions</h1>
      <table className="transactions-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Action</th>
            <th>Crypto</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Total</th>
            <th>Profit</th>
          </tr>
        </thead>
        <tbody>
          {data.map((txn, index) => {
            const total = txn.quantity * txn.price;
            const profit = 0; 

            return (
              <tr key={index}>
                <td>{new Date(txn.madeAt).toLocaleDateString()}</td>
                <td>{txn.action}</td>
                <td>{txn.currency}</td>
                <td>{txn.quantity}</td>
                <td>${Number(txn.price).toFixed(2)}</td>
                <td>${Number(total).toFixed(2)}</td>
                <td className={profit >= 0 ? "profit-positive" : "profit-negative"}>
                  ${Number(profit).toFixed(2)}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default Transactions;