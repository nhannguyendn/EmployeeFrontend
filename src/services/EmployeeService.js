import axios from "axios";

const EMPLOYEE_API_BASE_URL = "http://localhost:8888/api/v1/employees"
const EMPLOYEE_API_BASE_URL_LOGIN = "http://localhost:8888/api/v1/"

class EmployeeService {

    async getEmployees(token) {
        return await axios.get(EMPLOYEE_API_BASE_URL, {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true
        });
    }

    createEmployee(employee) {
        return axios.post(EMPLOYEE_API_BASE_URL, employee, {
            headers: {
                Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`,
                "Content-Type": "application/json;charset=UTF-8"
            },
            withCredentials: true
        });
    }

    async getEmployeeById(id, token) {
        return await axios.get(EMPLOYEE_API_BASE_URL + "/" + id, {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true
        });
    }

    async updateEmployee(id, employee, token) {
        return await axios.put(EMPLOYEE_API_BASE_URL + "/" + id, employee, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            withCredentials: true
        });
    }

    deleteEmployee(id) {
        return axios.delete(EMPLOYEE_API_BASE_URL + "/" + id, {
            headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` },
            withCredentials: true
        });
    }

    async login(email, password) {
        try {
            const res = await axios.post(EMPLOYEE_API_BASE_URL_LOGIN + "auth/login", { email, password });
            sessionStorage.setItem('accessToken', res.data.accessToken);
            sessionStorage.setItem('refreshToken', res.data.refreshToken);
            console.log("token:", !res.data.accessToken);
            return res.data.accessToken;
        } catch (err) {
            console.error("Login error:", err);
            //throw err;
            return null;
        }
    }

    async loginGoogle(googleToken) {
        try {
            console.log("token goole:", googleToken);
            const res = await axios.post(EMPLOYEE_API_BASE_URL_LOGIN + "auth/login-google", { googleToken }, {
                withCredentials: true
            });
            let accessToken = res.data.accessToken
            sessionStorage.setItem('accessToken', accessToken);
            sessionStorage.setItem('refreshToken', res.data.refreshToken);
            console.log("token:", accessToken);
            return accessToken;
        } catch (err) {
            console.error("Login error:", err);
            //throw err;
            return null;
        }
    }
}

const employeeService = new EmployeeService();
export default employeeService;
