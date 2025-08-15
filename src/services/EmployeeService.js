import axios from "axios";

const EMPLOYEE_API_BASE_URL = "http://localhost:8888/api/v1/employees"

class EmployeeService {

    getEmployees() {
        return axios.get(EMPLOYEE_API_BASE_URL);
    }

}

const employeeService = new EmployeeService();
export default employeeService;
