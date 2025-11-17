const STAGE = process.env.REACT_APP_STAGE;

const CONFIG = {
  development: { API_BASE_URL: "http://localhost:8888/api/v1" },
  staging: { API_BASE_URL: "http://localhost:9090/api/v1" },
  production: { API_BASE_URL: "http://springbootdemo.ap-southeast-2.elasticbeanstalk.com/api/v1" },
};

export const API_BASE_URL = CONFIG[STAGE].API_BASE_URL;