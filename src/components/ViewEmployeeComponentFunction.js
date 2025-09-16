import React, { useState, useEffect } from 'react';
import logo from '../logo.svg';
import employeeService from '../services/EmployeeService';
import { withNavigation } from '../withNavigation';
import { useEmployees } from '../hooks/useEmployees';
import { useNavigate } from "react-router-dom";

function ViewEmployeeComponentFunction({ params }) {
    //const [employee, setEmployee] = useState({});
    //const id = params.id;

    //   useEffect(() => {
    //     employeeService.getEmployeeById(id)
    //       .then(res => setEmployee(res.data));
    //   }, [id]);

    const id = params.id;
    const { employee, loading, error, accessToken, showLoading, getAccessToken, fetchEmployeeById } = useEmployees();
    const navigate = useNavigate();

    useEffect(() => {
        getAccessToken();
    }, []);

    useEffect(() => {
        if (accessToken) {
            fetchEmployeeById(id, accessToken);
        }
    }, [id, accessToken]);

    useEffect(() => {
        if (!loading && (!accessToken || !employee)) {
            navigate("/login");
        }
    }, [loading, accessToken, employee, navigate]);


    if (loading) return <p style={{ margin: "20px" }}>Đang tải dữ liệu...</p>;
    if (error) return <p style={{ margin: "20px" }}>Lỗi: {error.message}</p>;

    if (!employee) {
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
                            <div>{employee?.firstName}</div>
                        </div>
                        <div className='d-flex mb-2'>
                            <div style={{ minWidth: "120px", fontWeight: "bold", textAlign: "left" }}>LastName:</div>
                            <div>{employee?.lastName}</div>
                        </div>
                        <div className='d-flex mb-2'>
                            <div style={{ minWidth: "120px", fontWeight: "bold", textAlign: "left" }}>Email:</div>
                            <div>{employee?.emailId}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default withNavigation(ViewEmployeeComponentFunction);
