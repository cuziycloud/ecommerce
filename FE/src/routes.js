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
        path:"/ring",
        element:<ProductList categoryType={'RING'}/>,
    },
    {
      path:"/necklace",
      element:<ProductList categoryType={'NECKLACE'}/>,
    }
]);