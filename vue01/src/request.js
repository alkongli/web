
import axios from 'axios'
import router from "./router";

export function request(config) {

  const instance = axios.create({
    baseURL: "/api", //
  })
  // 直接返回
  return instance(config)

}
