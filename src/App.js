import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import ListEmployeeComponent from './components/ListEmployeeComponent';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import CreateEmployeeComponent from './components/CreateEmployeeComponent';
import UpdateEmployeeComponent from './components/UpdateEmployeeComponent';
import ViewEmployeeComponent from './components/ViewEmployeeComponent';

function App() {

  return (
    <div className="App">
      <Router>
        <div>
          <HeaderComponent />
        </div>

        <header className="App-header">
          <h3>Hello React!</h3>
          <img src={logo} className="App-logo" alt="logo" width={50} height={50} style={{ marginTop: '0px', marginBottom: '10px' }} />
          {/* <p>
            Edit <code>src/App.js</code> and save to reload.
          </p> */}
          <a className="App-link" href="https://reactjs.org" target="_blank" rel="noopener noreferrer" >React Page</a>
        </header>

        <main className='container'>

          <div>
            <Routes>
              <Route path="/" element={<ListEmployeeComponent />}></Route>
              <Route path="/employees" element={<ListEmployeeComponent />}></Route>
              <Route path="/add-employee/:id" element={<CreateEmployeeComponent />}></Route>
              <Route path="/view-employee/:id" element={<ViewEmployeeComponent />}></Route>
              <Route path="/update-employee/:id" element={<UpdateEmployeeComponent />}></Route>
            </Routes>
          </div>
        </main>

        <FooterComponent className="bg-dark text-white text-center p-2" />
      </Router>
    </div>
  )
}

export default App;
