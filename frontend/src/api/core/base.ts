// src/api/core/base.ts
import axios, { AxiosInstance } from 'axios'

export const createAxiosInstance = (baseURL: string): AxiosInstance => {
  return axios.create({
    baseURL,
    timeout: 10000,
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
