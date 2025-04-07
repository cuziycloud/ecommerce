import { configureStore } from '@reduxjs/toolkit';
import commonReducer from './slices/commonSlice';

export const store = configureStore({
  reducer: {
    commonState: commonReducer,
  },
});
