import { createSlice } from "@reduxjs/toolkit";

// const counterSlice = createSlice( same as before /);
const ovsessionSlice = createSlice({
  name: "ovsession",
  initialState: {
    value: 0,
    userid: 17,
    cake: 0,
  },
  reducers: {
    setOvSession: (state, action) => {
      state.value = action.payload;
    },
    setUserId: (state, action) => {
      state.userid = action.payload;
    },
    setCake: (state, action) => {
      state.cake = action.payload;
    },
  },
});

// destructure actions and reducer from the slice (or you can access as counterSlice.actions)
const { actions, reducer } = ovsessionSlice;

// export individual action creator functions
export const { setOvSession, setUserId, setCake } = actions;

// often the reducer is a default export, but that doesn't matter
export default reducer;