import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import ProductList from "./pages/productListPage/ProductList";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <App />
    },
    {
        path: "/rings",
        element: <ProductList />
         
    }
]);