import './App.css';
import {useEffect} from 'react';

function App() {

    useEffect(() => {
       fetch("http://localhost:8080/test")
           .then(d => d.json())
           .then(res => {
               console.log(res);
           })
           .catch(err => {
               console.log(err);
           })
    }, []);

  return (
    <div className="App">
        <nav id="my-nav">
            <h2>Big numbers</h2>
        </nav>
        <div id="main">
            <button className="btn btn-outline-info ml-auto">Upload XML</button>
        </div>

    </div>
  );
}

export default App;
