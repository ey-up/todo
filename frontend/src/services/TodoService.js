import GetAllTodosResponse from "../models/response/GetAllTodosResponse";
import Response from "../models/response/Response";
import { api } from "./TodoClient";

export const addTodo = async (request) => {
  try {
    const response = await api.post("/todos", request);
    console.log("Added Todo Successfully");
    response.data.success = true;
    return new Response(response);
  } catch (error) {
    console.log("Failed Todo Addition");
    return new Response(error?.response);
  }
};

export const deleteById = async (todoId) => {
  try {
    const response = await api.delete(`/todos/${todoId}`);
    console.log("Delete Todo Successfully");
    response.data = { success: true };
    return new Response(response);
  } catch (error) {
    console.log("Failed Todo Deletion");
    return new Response(error?.response);
  }
};

export const updateById = async (request, todoId) => {
  try {
    const response = await api.put(`/todos/${todoId}`, request);
    console.log("Update Todo Successfully");
    response.data.success = true;
    return new Response(response);
  } catch (error) {
    console.log("Failed Todo Updation");
    return new Response(error?.response);
  }
};

export const getByPage = async (page) => {
  try {
    const response = await api.get(`/todos/search?page=${page}&size=5`);
    response.data.success = true;
    const getAllTodos = new GetAllTodosResponse(response);
    return getAllTodos;
  } catch (error) {
    console.log("Failed Get by Page Todos");
    return new GetAllTodosResponse(error?.response);
  }
};

export const getByPageAndSearch = async (page, search) => {
  try {
    console.log("request start with search : ", search, " page: ", page);
    const response = await api.get(
      `/todos/search/${search}?page=${page}&size=5`
    );
    response.data.success = true;
    const getAllTodos = new GetAllTodosResponse(response);
    return getAllTodos;
  } catch (error) {
    console.log("Failed Get by Page and Search Todos");
    return new GetAllTodosResponse(error?.response);
  }
};
