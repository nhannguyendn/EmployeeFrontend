import React, { Component } from 'react';
import employeeService from '../services/EmployeeService';
import { withNavigation } from '../withNavigation'; // 

class LoginEmployeeComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: null,
            emailId: '',
            password: '',
            errorMessage: "",
            employee: {

            }
        }
    }

    cancel() {
        this.props.navigate("/employees");
    }

    loginGoolge() {

    }

    login = (e) => {
        e.preventDefault();

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!this.state.emailId || !emailRegex.test(this.state.emailId)) {
            this.setState({ errorMessage: "Invalid email address" });
            return;
        }


        if (!this.state.password || this.state.password.trim() === "") {
            this.setState({ errorMessage: "Password cannot be empty" });
            return;
        }

        let employee = { firstName: this.state.firstName, lastName: this.state.lastName, emailId: this.state.emailId };
        console.log("employee => " + JSON.stringify(employee));

        employeeService.login(this.state.emailId, this.state.password).then((res) => {
            //this.props.navigate(`/view-employee-function/${res.emailId}`);
            if (res == null || String(res).trim() === "") {
                this.setState({ errorMessage: "Email or Password is wrong" });
            } else {
                this.props.navigate('/employees');
            }
        });

    }

    componentDidMount() {
        if (this.state.id != null) {
            employeeService.getEmployeeById(this.state.id).then((res) => {
                this.setState({ employee: res.data });
            });
        }
    }

    onPasswordChange = (event) => {
        this.setState({ password: event.target.value });
    }

    onChangeEmailAddressHanlder = (event) => {
        this.setState({ emailId: event.target.value });
    }

    render() {
        return (
            <div>
                <div className='container' style={{ marginTop: "20px", width: "100%", marginLeft: "0px", marginRight: "0px" }}>
                    <div className='row'>

                        <div className='card col-md-6 offset-md-3 offset-md-3'>

                            <h3 className='text-center' style={{ margin: "10px" }}>Update Employee</h3>

                            <div className='card-body'>
                                <form>
                                    <div className='form-group' style={{ marginTop: "20px" }}>
                                        <label>Email Address:</label>
                                        <input placeholder='Email Address' name='emailId' className='form-control'
                                            value={this.state.emailId} onChange={this.onChangeEmailAddressHanlder} />
                                    </div>

                                    <div className='form-group' style={{ marginTop: "20px" }}>
                                        <label>Last Name:</label>
                                        <input type="password" placeholder='Password' name='password' className='form-control'
                                            value={this.state.password} onChange={this.onPasswordChange} />
                                    </div>

                                    <button className='btn-positive' style={{ marginTop: "10px", width: "150px" }} onClick={this.login}>Login</button>
                                    <button className='btn-cancel' style={{ marginTop: "10px", marginLeft: "10px" }} onClick={this.loginGoolge.bind(this)}>Login google</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        );
    }
}

export default withNavigation(LoginEmployeeComponent);