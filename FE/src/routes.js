import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import ProductList from "./pages/productListPage/ProductList";
import ShopApplicationWrapper from "./pages/ShopApplicationWrapper";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <ShopApplicationWrapper />,
        children: [
            {
                path: "/",
                element: <App />
            }
        ]
    },
    {
        path:"/rings",
        element:<ProductList categoryType={'RINGS'}/>,
    },
    {
      path:"/bracelets",
      element:<ProductList categoryType={'BRACELETS'}/>,
    }
]);