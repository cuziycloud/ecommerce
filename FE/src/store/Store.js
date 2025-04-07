import { combineReducers, configureStore } from "@reduxjs/toolkit";
import productReducer from './features/Product'


const rootReducer = combineReducers({
    productState: productReducer,
})

const store = configureStore({
    reducer : rootReducer
})

export default store;