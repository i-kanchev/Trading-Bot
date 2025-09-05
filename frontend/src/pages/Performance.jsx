import '../css/Performance.css'

function Performance() {
  const data = [
    { date: "2025-09-01", revenue: 500 },
    { date: "2025-09-02", revenue: 700 },
    { date: "2025-09-03", revenue: -100 },
    { date: "2025-09-04", revenue: 450 },
    { date: "2025-09-05", revenue: 0 },
  ];

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
              <td>{entry.date}</td>
              <td className={entry.revenue >= 0 ? "revenue-positive" : "revenue-negative"}>
                ${entry.revenue}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Performance;