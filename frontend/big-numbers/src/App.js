import './App.css';
import {useEffect} from 'react';
import { useAlert } from 'react-alert'

function App() {

    const alert = useAlert();

    useEffect(() => {
       fetch("http://localhost:8080/test", {
           headers: {
               'Content-Type': 'application/json'
           }
       })
           .then(d => d.json())
           .then(res => {
                console.log(res);
           })
           .catch(err => {
               console.log(err);
               alert.error("Server not accessible")
           })
    }, []);

  return (
    <div className="App">
        <nav id="my-nav">
            <h2>BIG numbers</h2>
        </nav>
        <div id="main">
            <div style={{textAlign: 'right'}}>
                <button className="btn btn-outline-info ml-auto">Upload XML</button>
            </div>
            <div style={{height: '50vh', marginTop: '200px'}} className="text-center w-100 " >
                <form id="numbers-form">
                    <textarea id="inputBox" />
                    <div className="text-right">
                        <button className="btn btn-info" type="submit">Compute</button>
                    </div>
                    <h4 style={{marginTop: '20px', borderBottom: '5px solid black', width: 'fit-content'}}>Result is: 30000989787</h4>
                </form>


            </div>
        </div>

    </div>
  );
}

export default App;
