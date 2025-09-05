import '../css/Transactions.css'

function Transactions() {
  const data = [
    { date: "2025-09-01", action: "Buy", quantity: 10, price: 50, total: 500, profit: 0 },
    { date: "2025-09-02", action: "Sell", quantity: 5, price: 70, total: 350, profit: 100 },
    { date: "2025-09-03", action: "Buy", quantity: 8, price: 40, total: 320, profit: 0 },
    { date: "2025-09-04", action: "Sell", quantity: 8, price: 60, total: 480, profit: 160 },
    { date: "2025-09-05", action: "Sell", quantity: 3, price: 30, total: 90, profit: -50 },
  ];

  return (
    <div className="transactions-container">
      <h1 className="transactions-title">Transactions</h1>
      <table className="transactions-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Action</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Total</th>
            <th>Profit</th>
          </tr>
        </thead>
        <tbody>
          {data.map((txn, index) => (
            <tr key={index}>
              <td>{txn.date}</td>
              <td>{txn.action}</td>
              <td>{txn.quantity}</td>
              <td>${txn.price}</td>
              <td>${txn.total}</td>
              <td className={txn.profit >= 0 ? "profit-positive" : "profit-negative"}>
                ${txn.profit}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Transactions;