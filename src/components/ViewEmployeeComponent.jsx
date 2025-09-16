import React, { Component } from 'react';
import logo from '../logo.svg';
import PropTypes from 'prop-types';
import employeeService from '../services/EmployeeService';
import { withNavigation } from '../withNavigation'; // ✅ đi từ components ra src

class ViewEmployeeComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.params.id,
            employee: {

            },
            token: sessionStorage.getItem("accessToken")
        }
    }

    componentDidMount() {
        let token = this.state.token;
        if (!token || token.trim() === "") {
            setTimeout(() => this.props.navigate("/login"), 0);
        } else {
            employeeService.getEmployeeById(this.state.id, token).then((res) => {
                this.setState({ employee: res.data });
            });
        }
    }

    render() {
        if (!this.state.token || this.state.token.trim() === "") {
            return null;
        }
        return (
            <div>
                <h2 style={{ margin: "20px" }}>View Employee Page</h2>

                <div className='card col-md-6 offset-md-3'>
                    <div className='card-body d-flex'>
                        <img
                            src={logo}
                            className="App-avatar"
                            alt="logo"
                            width={60}
                            height={60}
                            style={{ marginRight: '15px', marginTop: '10px', marginBottom: '10px' }}
                        />

                        <div className='flex-grow-1'>

                            <div className='d-flex mb-2'>
                                <div style={{ minWidth: "120px", fontWeight: "bold", textAlign: "left" }}>FirstName:</div>
                                <div>{this.state.employee.firstName}</div>
                            </div>
                            <div className='d-flex mb-2'>
                                <div style={{ minWidth: "120px", fontWeight: "bold", textAlign: "left" }}>LastName:</div>
                                <div>{this.state.employee.lastName}</div>
                            </div>
                            <div className='d-flex mb-2'>
                                <div style={{ minWidth: "120px", fontWeight: "bold", textAlign: "left" }}>Email:</div>
                                <div>{this.state.employee.emailId}</div>
                            </div>
                        </div>
                    </div>
                </div>

            </div >
        );
    }
}

export default withNavigation(ViewEmployeeComponent);