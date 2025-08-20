import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withNavigation } from '../withNavigation';
import EmployeeService from '../services/EmployeeService';

class CreateEmployeeComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.params.id,
            firstName: '',
            lastName: '',
            emailId: ''
        }

        this.onChangeFirstNameHandler = this.onChangeFirstNameHandler.bind(this);
        this.onChangeLastNameHanlder = this.onChangeLastNameHanlder.bind(this);
        this.onChangeEmailAddressHanlder = this.onChangeEmailAddressHanlder.bind(this);
        this.saveEmployee = this.saveEmployee.bind(this);
    }

    componentDidMount() {
        if (this.state.id !== 'new') {
            EmployeeService.getEmployeeById(this.state.id).then((res) => {
                let employee = res.data;
                this.setState({
                    firstName: employee.firstName,
                    lastName: employee.lastName,
                    emailId: employee.emailId
                })
            })
        }
    }

    saveEmployee = (e) => {
        e.preventDefault();

        let employee = { firstName: this.state.firstName, lastName: this.state.lastName, emailId: this.state.emailId };
        console.log("employee => " + JSON.stringify(employee));

        if (this.state.id === 'new') {
            EmployeeService.createEmployee(employee).then((res) => {
                this.props.navigate("/employees");
            })
        } else {
            EmployeeService.updateEmployee(this.state.id, employee).then((res) => {
                this.props.navigate("/employees");
            })
        }
    }

    getTitle() {
        if (this.state.id === 'new') {
            return <h3 className='text-center' style={{ margin: "10px" }}>Add Employee</h3>;
        } else {
            return <h3 className='text-center' style={{ margin: "10px" }}>Add Employee</h3>;
        }
    }

    getButtonText() {
        if (this.state.id === 'new') {
            return <button className='btn-positive' style={{ marginTop: "10px" }} onClick={this.saveEmployee}>Add</button>;
        } else {
            return <button className='btn-positive' style={{ marginTop: "10px" }} onClick={this.saveEmployee}>Update</button>;
        }
    }

    cancel() {
        this.props.navigate("/employees");
    }

    onChangeFirstNameHandler = (event) => {
        this.setState({ firstName: event.target.value });
    }

    onChangeLastNameHanlder = (event) => {
        this.setState({ lastName: event.target.value });
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

                            {
                                this.getTitle()
                            }

                            <div className='card-body'>
                                <form>
                                    <div className='form-group'>
                                        <label>First Name:</label>
                                        <input placeholder='First Name' name='firstName' className='form-control'
                                            value={this.state.firstName} onChange={this.onChangeFirstNameHandler} />
                                    </div>

                                    <div className='form-group' style={{ marginTop: "20px" }}>
                                        <label>Last Name:</label>
                                        <input placeholder='Last name' name='lastName' className='form-control'
                                            value={this.state.lastName} onChange={this.onChangeLastNameHanlder} />
                                    </div>

                                    <div className='form-group' style={{ marginTop: "20px" }}>
                                        <label>Email Address:</label>
                                        <input placeholder='Email Address' name='emailId' className='form-control'
                                            value={this.state.emailId} onChange={this.onChangeEmailAddressHanlder} />
                                    </div>

                                    {
                                        this.getButtonText()
                                    }
                                    <button className='btn-cancel' style={{ marginTop: "10px", marginLeft: "10px" }} onClick={this.cancel.bind(this)}>Cancel</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        );
    }
}

export default withNavigation(CreateEmployeeComponent);