// src/api/core/http.ts
import { AxiosInstance, Method, AxiosRequestConfig } from 'axios'

export const createApiMethod =
  (instance: AxiosInstance, method: Method) =>
  <T>(config: AxiosRequestConfig): Promise<T> =>
    instance({ ...config, method }).then(res => res.data)

export const createHttpClient = (instance: AxiosInstance) => ({
  get: createApiMethod(instance, 'GET'),
  post: createApiMethod(instance, 'POST'),
  put: createApiMethod(instance, 'PUT'),
  patch: createApiMethod(instance, 'PATCH'),
  delete: createApiMethod(instance, 'DELETE'),
})
