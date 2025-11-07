import { useState, useEffect } from "react";
import axios from "axios";
import logo from '../logo.svg';
import employeeService from "../services/EmployeeService";

const EMPLOYEE_API_BASE_URL = "http://localhost:8888/api/v1/employees";

export function useEmployees() {
  const [avatarUrl, setAvatarUrl] = useState(logo);    // url avatar
  const [employees, setEmployees] = useState([]);    // list employees
  const [employee, setEmployee] = useState(null);    // single employee
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [accessToken, setAccessToken] = useState(null);

  // Lấy tất cả employees
  const fetchEmployees = async () => {
    try {
      setLoading(true);
      const res = await axios.get(EMPLOYEE_API_BASE_URL);
      setEmployees(res.data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  // Lấy employee theo ID
  const fetchEmployeeById = async (id, token) => {
    try {
      setLoading(true);
      const res = await employeeService.getEmployeeById(id, token);
      setEmployee(res.data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  // Thêm employee
  const createEmployee = async (employee) => {
    await axios.post(EMPLOYEE_API_BASE_URL, employee);
    fetchEmployees();
  };

  // Cập nhật employee
  const updateEmployee = async (id, employee) => {
    await axios.put(`${EMPLOYEE_API_BASE_URL}/${id}`, employee);
    fetchEmployees();
  };

  // Xoá employee
  const deleteEmployee = async (id) => {
    await axios.delete(`${EMPLOYEE_API_BASE_URL}/${id}`);
    fetchEmployees();
  };

  // useEffect(() => {
  //   fetchEmployees();
  // }, []);

  const getAccessToken = (() => {
    setLoading(true);
    setAccessToken(sessionStorage.getItem("accessToken"));
  });

  const showLoading = (() => {
    setLoading(true);
  })

  const uploadAvatar = async (id, file) => {

    if (file.size > 1 * 1024 * 1024) { // > 1MB
      setError("File size exceeds 1MB limit.");
      return;
    }

    try {
      setLoading(true);
      await new Promise(resolve => setTimeout(resolve, 2000)); // delay 2s
      const res = await employeeService.uploadAvatar(file, id);
      setAvatarUrl(res.data);
    } catch (err) {
      if (err.response && err.response.status === 413) {
        setError("File size exceeds the allowed limit (e.g. 10MB).");
      } else {
        setError(err.response?.data || "Upload failed!");
      }
    } finally {
      setLoading(false);
    }
  };

  const resetError = () => {
    setError(null)
  }

  const updateAvatar = (url) => {
    setAvatarUrl(url);
  }

  return {
    employees,
    employee,
    avatarUrl,
    loading,
    error,
    accessToken,
    showLoading,
    getAccessToken,
    fetchEmployees,
    fetchEmployeeById,
    uploadAvatar,
    createEmployee,
    updateEmployee,
    deleteEmployee,
    resetError,
    updateAvatar
  };
}
