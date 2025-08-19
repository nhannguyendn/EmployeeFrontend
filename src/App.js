import logo from './logo.svg';
import './App.css';
import ListEmployeeComponent from './components/ListEmployeeComponent';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';

function App() {
  // return (
  //   <div className="App">
  //     <header className="App-header">
  //       <h1>Hello word!</h1>
  //       <img src={logo} className="App-logo" alt="logo" />
  //       <p>
  //         Edit <code>src/App.js</code> and save to reload.
  //       </p>
  //       <a
  //         className="App-link"
  //         href="https://reactjs.org"
  //         target="_blank"
  //         rel="noopener noreferrer"
  //       >
  //         Learn React
  //       </a>
  //     </header>
  //   </div>
  // );

  return (
    <div className="App">
      <div>
        <HeaderComponent />
      </div>

      <header className="App-header">
        <h1>Hello word!</h1>
        <img src={logo} className="App-logo" alt="logo" width={100} height={100} style={{ marginTop: '0px', marginBottom: '10px' }} />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>

      <main className='container'>
        <ListEmployeeComponent />
      </main>

      <div>
        <FooterComponent className="bg-dark text-white text-center p-2"/>
      </div>
    </div>
  )
}

export default App;
