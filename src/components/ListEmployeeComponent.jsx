import React, { Component } from 'react';
import EmployeeService from '../services/EmployeeService';
import { withNavigation } from "../withNavigation"; // ✅ đi từ components ra src

class ListEmployeeComponent extends Component {

    constructor(props) {
        super(props);

        this.state = { employees: [], token: sessionStorage.getItem("accessToken") };

        this.addEmployee = this.addEmployee.bind(this);
        this.updateEmployee = this.updateEmployee.bind(this);
        this.deleteEmployee = this.deleteEmployee.bind(this);
        this.viewEmployee = this.viewEmployee.bind(this);
        this.logout = this.logout.bind(this);
    }

    logout() {
        EmployeeService.logout(this.state.token).then((res) => {
            this.props.navigate("/login");
        }).catch((err) => {
            if (err.response && (err.response.status === 401 || err.response.status === 403 || err.response.status === 500)) {
                sessionStorage.removeItem("accessToken");
                this.props.navigate("/login");
            } else {
                console.error(err);
            }
        });;
    }

    addEmployee = (e) => {
        this.props.navigate("/add-employee/new");
    }

    updateEmployee(id) {
        this.props.navigate(`/add-employee/${id}`)
        //this.props.navigate(`/update-employee/${id}`)
    }

    deleteEmployee(id) {
        EmployeeService.deleteEmployee(id).then((res) => {
            this.setState({ employees: this.state.employees.filter(employee => employee.id !== id) });
        });
    }

    viewEmployee(id) {
        //this.props.navigate(`/view-employee/${id}`); // ViewEmployeeComponent: class component with state
        this.props.navigate(`/view-employee-function/${id}`); // ViewEmployeeComponentFunction: Function component with useState + useEffect
    }

    componentDidMount() {
        let token = this.state.token;
        if (!token || token.trim() === "") {
            setTimeout(() => this.props.navigate("/login"), 0);
        } else {
            EmployeeService.getEmployees(token).then((res) => {
                this.setState({ employees: res.data.data });
            }).catch((err) => {
                if (err.response && (err.response.status === 401 || err.response.status === 403 || err.response.status === 500)) {
                    sessionStorage.removeItem("accessToken");
                    this.props.navigate("/login");
                } else {
                    console.error(err);
                }
            });;
        }
    }

    render() {
        if (!this.state.token || this.state.token.trim() === "") {
            return null;
        }
        return (
            <div>
                <h2 className='text_center' style={{ margin: "50px" }}>Employees List</h2>
                <div className='row' style={{ display: "flex", justifyContent: "space-between", marginBottom: "10px" }}>
                    <button className='btn btn-primary' onClick={this.addEmployee} style={{ width: "auto" }}>Add Employee</button>
                    <button className='btn-cancel' onClick={this.logout} style={{ width: "auto" }}>Logout</button>
                </div>
                <div className='row'>
                    <table className='table table-striped table-bordered'>

                        <thead>
                            <tr>
                                <th>Id employee</th>
                                <th>First name employee</th>
                                <th>Last name employee</th>
                                <th>Email employee</th>
                                <th>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            {
                                this.state.employees?.map(
                                    employee =>
                                        <tr key={employee.id}>
                                            <td>{employee.id}</td>
                                            <td>{employee.firstName}</td>
                                            <td>{employee.lastName}</td>
                                            <td>{employee.emailId}</td>
                                            <td>
                                                <button className="btn-positive" onClick={() => this.viewEmployee(employee.id)} >View</button>
                                                <button className="btn-positive" onClick={() => this.updateEmployee(employee.id)} style={{ marginLeft: "10px" }}>Update</button>
                                                <button className="btn-cancel" onClick={() => this.deleteEmployee(employee.id)} style={{ marginLeft: "10px" }}>Delete</button>
                                            </td>
                                        </tr>
                                )

                            }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default withNavigation(ListEmployeeComponent);