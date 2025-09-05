import "../css/Training.css";
import { useState } from "react";

function Training() {
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [budget, setBudget] = useState("");
  const [finalRevenue, setFinalRevenue] = useState(null);

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!startDate || !endDate || !budget) return;

    const start = new Date(startDate);
    const end = new Date(endDate);

    if (end <= start) {
      alert("End date must be after start date");
      return;
    }

    const budgetNumber = parseFloat(budget);

    const finalRevenueCalc = (budgetNumber * 2).toFixed(2);

    setFinalRevenue(finalRevenueCalc);
  };

  return (
    <div className="training-container">
      <h1 className="training-title">Training</h1>
      <form className="training-form" onSubmit={handleSubmit}>
        <label>Start Date and Time</label>
        <input
          type="datetime-local"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
        />

        <label>End Date and Time</label>
        <input
          type="datetime-local"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
        />

        <label>Budget</label>
        <input
          type="number"
          value={budget}
          onChange={(e) => setBudget(e.target.value)}
          min="0"
          step="1"
        />

        <button type="submit">Calculate</button>
      </form>

      {finalRevenue !== null && (
        <div className="results-container">
          <div className="result-field">Final Revenue: ${finalRevenue}</div>
        </div>
      )}
    </div>
  );
}

export default Training;
