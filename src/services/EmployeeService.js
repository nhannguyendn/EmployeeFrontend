import axios from "axios";
import axiosClient from "../axiosClient";
import { API_BASE_URL } from "../config";

//const EMPLOYEE_API_BASE_URL_EMPLOYEE = "http://localhost:8888/api/v1/employees"
//const EMPLOYEE_API_BASE_URL = "http://localhost:8888/api/v1/"
const EMPLOYEES_PATH = "/employees";

class EmployeeService {

    async getEmployees(token) {
        return await axiosClient.get(EMPLOYEES_PATH);;
    }

    createEmployee(employee) {
        //        return axios.post(EMPLOYEE_API_BASE_URL_EMPLOYEE, employee, {
        //            headers: {
        //                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
        //                "Content-Type": "application/json;charset=UTF-8"
        //            },
        //            withCredentials: true
        //        });

        return axiosClient.post(EMPLOYEES_PATH, employee, {
            headers: { "Content-Type": "application/json;charset=UTF-8" }
        });
    }

    async getEmployeeById(id, token) {
        // return await axios.get(EMPLOYEE_API_BASE_URL_EMPLOYEE + "/" + id, {
        //     headers: { Authorization: `Bearer ${token}` },
        //     withCredentials: true
        // });

        return await axiosClient.get(EMPLOYEES_PATH + "/" + id);
    }

    async updateEmployee(id, employee, token) {
        // return await axios.put(EMPLOYEE_API_BASE_URL_EMPLOYEE + "/" + id, employee, {
        //     headers: {
        //         Authorization: `Bearer ${token}`,
        //         "Content-Type": "application/json"
        //     },
        //     withCredentials: true
        // });

        return await axios.put(EMPLOYEES_PATH + "/" + id, employee, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json"
            },
        });
    }

    deleteEmployee(id) {
        // return axios.delete(EMPLOYEE_API_BASE_URL_EMPLOYEE + "/" + id, {
        //     headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` },
        //     withCredentials: true
        // });
        return axiosClient.delete(EMPLOYEES_PATH + "/" + id);
    }

    async login(email, password) {
        try {
            //const res = await axios.post(EMPLOYEE_API_BASE_URL + "auth/login", { email, password });
            const res = await axios.post(API_BASE_URL + "/auth/login", { email, password });
            sessionStorage.setItem('accessToken', res.data.data.accessToken);
            sessionStorage.setItem('refreshToken', res.data.data.refreshToken);
            return res.data.data.accessToken;
        } catch (err) {
            console.error("Login error:", err);
            //throw err;
            return null;
        }
    }

    async loginGoogle(googleToken) {
        try {
            // const res = await axios.post(EMPLOYEE_API_BASE_URL + "auth/login-google", { googleToken }, {
            //     withCredentials: true
            // });
            const res = await axiosClient.post(API_BASE_URL + "/auth/login-google", { googleToken });
            let accessToken = res.data.data.accessToken
            sessionStorage.setItem('accessToken', accessToken);
            sessionStorage.setItem('refreshToken', res.data.data.refreshToken);
            return accessToken;
        } catch (err) {
            console.error("Login error:", err);
            //throw err;
            return null;
        }
    }

    async logout(token) {
        try {
            console.log("token logout:" + token);

            // const res = await axios.post(
            //     EMPLOYEE_API_BASE_URL + "auth/logout",
            //     {},
            //     {
            //         headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` },
            //         withCredentials: true
            //     }
            // );

            const res = await axiosClient.post("/auth/logout", {});


            if (res.data.success) {
                sessionStorage.removeItem("accessToken");
                sessionStorage.removeItem("refreshToken");
                return true;
            }
            return false;
        } catch (err) {
            console.error("Logout error:", err);
            //throw err;
            return false;
        }
    }

    async uploadAvatar(file, employeeId) {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("employeeId", employeeId);

        // return await axios.post(
        //     EMPLOYEE_API_BASE_URL_EMPLOYEE + "/avatar/local",
        //     formData,
        //     {
        //         headers: {
        //             Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
        //             "Content-Type": "multipart/form-data",
        //         },
        //         withCredentials: true,
        //     }
        // );

        return await axiosClient.post(
            EMPLOYEES_PATH + "/avatar/local",
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            }
        );
    }
}

const employeeService = new EmployeeService();
export default employeeService;
