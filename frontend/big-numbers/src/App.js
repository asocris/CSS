import './App.css';
import React from 'react';
import {useEffect, useState} from 'react';
import {useAlert} from 'react-alert'

function App() {

    const alert = useAlert();
    const [inputText, setInputText] = useState(null);
    const [verifyTimeout, setVerifyTimeout] = useState(null);
    const [variables, setVariables] = useState([]);
    const [result, setResult] = useState('');
    const [canCompute, setCanCompute] = useState(false);

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


    const uniqueValues = arr => arr.filter((v, i, a) => a.indexOf(v) === i);


    const verifyInput = (input) => {
        return true;
    };

    const changeVariable = e => {
        const newVariables = [...variables];
        const varValue = e.target.value;
        const varName = e.target.parentElement.getAttribute('data-variable');
        for (let v of newVariables) {
            if (v.name === varName) {
                v.value = varValue;
            }
        }
        setVariables(newVariables);
    };

    const computeInput = (input) => {
        if (verifyInput(input)) {
            const R = /[a-zA-Z]+/g;
            let wordVariables = uniqueValues(input.match(R));
            const newVariables = [];
            const variablesNames = variables.map(v => v.name);
            for (let v of variables) {
                if (wordVariables.includes(v.name)) {
                    newVariables.push(v);
                }
            }
            for (let w of wordVariables) {
                if (!variablesNames.includes(w)) {
                    newVariables.push({
                        name: w,
                        value: ''
                    })
                }
            }
            setVariables(newVariables);
        }
        setResult(input);
        setCanCompute(true);
    };

    const inputChange = e => {
        setInputText(e.target.value);
        setCanCompute(false);

        const timeout = setTimeout(() => {
            computeInput(e.target.value);
        }, 3000);

        if (verifyTimeout) {
            clearTimeout(verifyTimeout);
            setVerifyTimeout(timeout);
        } else {
            setVerifyTimeout(timeout);
        }

    };


    const objectFromArray = arr => {
        let obj = {};
        arr.forEach(e => {
            obj[e.name] = e.value;
        });
        return obj;
    };


    const verifyExpression = () => {
        function isLetter(str) {
            return str.length === 1 && str.match(/[a-zA-Z]/i);
        }

        function isSign(str) {
            const SIGNS = ['+', '-', '*', '/', '^'];
            return SIGNS.includes(str);
        }

        let openPar = 0;
        for (let c of inputText) {
            if (c === '(') {
                openPar++;
            } else if (c === ')') {
                openPar--;
                if (openPar < 0) {
                    alert.error("Wrong expression parenthesis!");
                    return false;
                }
            } else if (!isLetter(c) && !isSign(c)) {
                alert.error("Expression must contain only parenthesis, letters and math signs!");
                return false;
            }
        }
        if (openPar !== 0) {
            alert.error("Wrong expression parenthesis!");
            return false;
        }
        return true;
    };

    const verifyVariables = () => {
        function isNumber(n) {
            return /^-?[\d.]+(?:e-?\d+)?$/.test(n);
        }

        for (let variable of variables) {
            if (variable.name.length !== 1) {
                alert.error("Variables names must have only one letter!");
                return false;
            }
            if (variable.value.length < 1) {
                alert.error("All variables must have a value!");
                return false;
            }
            if (!isNumber(variable.value)) {
                alert.error("All variables must have numeric values!");
                return false;
            }
        }
        return true;
    };

    const verifyData = () => {
        if (!verifyExpression() || !verifyVariables()) {
            return false;
        }
        return true;
    };

    const submitForm = e => {
        e.preventDefault();

        if (!verifyData()) {
            return;
        }

        fetch("http://localhost:8080/compute", {
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'POST',
            body: JSON.stringify({
                expression: inputText.replace(/\s+/g, ''),
                variables: objectFromArray(variables)
            })
        })
            .then(d => d.text())
            .then(res => {
                console.log(res);
                alert.success(res);
            })
            .catch(err => {
                console.log(err);
                alert.error(err.message || err.Message);
            })
    };

    return (
        <div className="App">
            <nav id="my-nav">
                <h2>BIG numbers</h2>
            </nav>
            <div id="main">
                <div style={{textAlign: 'right'}}>
                    <button className="btn btn-outline-info ml-auto">Upload XML</button>
                </div>
                <div style={{height: '50vh', marginTop: '100px'}} className="text-center w-100 ">
                    <form id="numbers-form" onSubmit={submitForm} spellCheck="false">
                        <textarea id="inputBox" onChange={inputChange}/>
                        <div className="mt-2">
                            {
                                variables.map(v => {
                                    return (
                                        <div data-variable={v.name} key={v.name} className="d-flex mb-3">
                                            <input value={v.name} disabled className="width-10 mr-5 pl-2"/>
                                            <input className="w-100 variable-input" onChange={changeVariable}/>
                                        </div>
                                    )
                                })
                            }
                        </div>
                        <div className="text-right">
                            <button className="btn btn-info" type="submit" disabled={!canCompute}>Compute</button>
                        </div>
                        <h4 style={{marginTop: '20px', borderBottom: '5px solid black', width: 'fit-content'}}>Result is: {result}</h4>
                    </form>


                </div>
            </div>

        </div>
    );
}

export default App;
