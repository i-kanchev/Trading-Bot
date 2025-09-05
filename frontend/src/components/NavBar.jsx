import '../css/NavBar.css'
import { Link } from "react-router-dom";

function NavBar() {
    return <nav className="navbar">
        <Link to="/" className="nav-link">Home</Link>
        <Link to="/training" className="nav-link">Training</Link>
        <Link to="/trading" className="nav-link">Trading</Link>
        <Link to="/transactions" className="nav-link">Transactions</Link>
        <Link to="/performance" className="nav-link">Performance</Link>
    </nav>
}

export default NavBar