import axios from "axios";

const EMPLOYEE_API_BASE_URL = "http://localhost:8888/api/v1/employees"
const EMPLOYEE_API_BASE_URL_LOGIN = "http://localhost:8888/api/v1/"

class EmployeeService {

    async getEmployees() {
        const token = sessionStorage.getItem('accessToken');
        console.log("token:", token);
        if (!token) throw new Error("Not logged in");
        return await axios.get(EMPLOYEE_API_BASE_URL, {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true
        });
    }

    createEmployee(employee) {
        return axios.post(EMPLOYEE_API_BASE_URL, employee, {
            headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` }
        });
    }

    getEmployeeById(id) {
        return axios.get(EMPLOYEE_API_BASE_URL + "/" + id, {
            headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` }
        });
    }

    updateEmployee(id, employee) {
        return axios.put(EMPLOYEE_API_BASE_URL + "/" + id, employee, {
            headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` }
        });
    }

    deleteEmployee(id) {
        return axios.delete(EMPLOYEE_API_BASE_URL + "/" + id, {
            headers: { Authorization: `Bearer ${sessionStorage.getItem('accessToken')}` }
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
}

const employeeService = new EmployeeService();
export default employeeService;
