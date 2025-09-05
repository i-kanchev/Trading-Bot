import './css/App.css'
import Home from './pages/Home'
import Trading from './pages/Trading'
import Training from './pages/Training'
import Performance from './pages/Performance'
import Transactions from './pages/Transactions'
import { Routes, Route } from 'react-router-dom'
import NavBar from './components/NavBar'

function App() {
  return (
    <div>
      <NavBar/>
      <main className='main-content'>
        <Routes>
          <Route path='/' element={<Home/>}/>
          <Route path='/performance' element={<Performance/>}/>
          <Route path='/trading' element={<Trading/>}/>
          <Route path='/training' element={<Training/>}/>
          <Route path='/transactions' element={<Transactions/>}/>
        </Routes>
      </main>
    </div>
  )
}

export default App
