import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import ShopApplicationWrapper from "./pages/ShopApplicationWrapper";
import { loadProductBySlug } from "./routes/products";
import ProductDetails from "./pages/ProductDetailPage/ProductDetails";
import ProductList from "./pages/productListPage/ProductList";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <ShopApplicationWrapper />,
        children: [
            {
                path: "/",
                element: <App />
            },
            {
                path:"/rings",
                element:<ProductList categoryType={'RINGS'}/>,
            },
            {
              path:"/bracelets",
              element:<ProductList categoryType={'BRACELETS'}/>,
            },
            {
                path:"/necklaces",
                element:<ProductList categoryType={'NECKLACES'}/>,
            },
            {
              path:"/earrings",
              element:<ProductList categoryType={'EARRINGS'}/>,
            },
            {
                path:"/product/:slug",
                loader: loadProductBySlug,
                element: <ProductDetails />
            }
        ]
    }
]);