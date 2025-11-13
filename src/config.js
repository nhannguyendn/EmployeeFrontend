const ENV = process.env.NODE_ENV;

const CONFIG = {
  development: {
    API_BASE_URL: "http://localhost:8888/api/v1",
  },
  production: {
    API_BASE_URL: "http://springbootdemo.ap-southeast-2.elasticbeanstalk.com/api/v1",
  },
};

export const API_BASE_URL = CONFIG[ENV].API_BASE_URL;
