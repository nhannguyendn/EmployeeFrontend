import React, { useState, useEffect } from 'react';
import logo from '../logo.svg';
import employeeService from '../services/EmployeeService';
import { withNavigation } from '../withNavigation';
import { useEmployees } from '../hooks/useEmployees';
import { useNavigate } from "react-router-dom";

function ViewEmployeeComponentFunction({ params }) {
    const id = params.id;
    const { employee, avatarUrl, loading, error, accessToken, showLoading,
        getAccessToken, fetchEmployeeById, uploadAvatar, resetError, updateAvatar } = useEmployees();
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

    useEffect(() => {
        if (employee && employee.avatar) updateAvatar(employee.avatar);
        else updateAvatar(logo);
    }, [employee]);

    const handleAvatarClick = () => {
        resetError();
        document.getElementById("avatarInput").click();
    };

    const handleAvatarChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        try {
            //const response = await employeeService.uploadAvatar(file, id);
            //setAvatarUrl(response.data);
            uploadAvatar(id, file)
        } catch (error) {
            console.error("Upload failed:", error);
        }
    };

    //if (loading) return <p style={{ margin: "20px" }}>Đang tải dữ liệu...</p>;
    //if (error) return <p style={{ margin: "20px" }}>Lỗi: {error.message}</p>;
    if (!employee) return null;

    return (
        <div>
            <h2 style={{ margin: "20px" }}>View Employee Page</h2>

            {loading && <p style={{ margin: "20px", color: "#555" }}>Uploading avatar...</p>}
            {error && <p style={{ margin: "20px", color: "red" }}>Lỗi: {error.message || error}</p>}

            <div className='card col-md-6 offset-md-3'>
                <div className='card-body d-flex'>
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

                    {/* Avatar section */}
                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                        <img
                            src={avatarUrl}
                            alt="avatar"
                            width={80}
                            height={80}
                            style={{ borderRadius: '50%', cursor: 'pointer', objectFit: 'cover' }}
                            onClick={handleAvatarClick}
                            onError={(e) => { e.target.src = logo; }}
                        />
                        <input
                            type="file"
                            id="avatarInput"
                            style={{ display: 'none' }}
                            accept="image/*"
                            onChange={handleAvatarChange}
                        />
                        <small style={{ marginTop: 6, color: '#888' }}>Change avatar</small>
                    </div>

                </div>
            </div>
        </div>
    );
}

export default withNavigation(ViewEmployeeComponentFunction);
