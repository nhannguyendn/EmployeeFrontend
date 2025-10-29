import axios from "axios";

const EMPLOYEE_API_BASE_URL_EMPLOYEE = "http://localhost:8888/api/v1/employees"
const EMPLOYEE_API_BASE_URL = "http://localhost:8888/api/v1/"

class EmployeeService {

    async getEmployees(token) {
        return await axios.get(EMPLOYEE_API_BASE_URL_EMPLOYEE, {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true
        });
    }

    createEmployee(employee) {
        return axios.post(EMPLOYEE_API_BASE_URL_EMPLOYEE, employee, {
            headers: {
                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
                "Content-Type": "application/json;charset=UTF-8"
            },
            withCredentials: true
        });
    }

    async getEmployeeById(id, token) {
        return await axios.get(EMPLOYEE_API_BASE_URL_EMPLOYEE + "/" + id, {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true
        });
    }

    async updateEmployee(id, employee, token) {
        return await axios.put(EMPLOYEE_API_BASE_URL_EMPLOYEE + "/" + id, employee, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            withCredentials: true
        });
    }

    deleteEmployee(id) {
        return axios.delete(EMPLOYEE_API_BASE_URL_EMPLOYEE + "/" + id, {
            headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` },
            withCredentials: true
        });
    }

    async login(email, password) {
        try {
            const res = await axios.post(EMPLOYEE_API_BASE_URL + "auth/login", { email, password });
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
            const res = await axios.post(EMPLOYEE_API_BASE_URL + "auth/login-google", { googleToken }, {
                withCredentials: true
            });
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
            const res = await axios.post(
                EMPLOYEE_API_BASE_URL + "auth/logout",
                {},
                {
                    headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` },
                    withCredentials: true
                }
            );
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
}

const employeeService = new EmployeeService();
export default employeeService;
