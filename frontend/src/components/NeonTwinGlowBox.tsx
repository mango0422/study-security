// import React from 'react'

// export default function NeonTwinGlowBox() {
//   return (
//     <div className="relative flex h-12 w-48 items-center justify-center rounded-md bg-black">
//       <svg
//         className="pointer-events-none absolute top-0 left-0 h-full w-full"
//         viewBox="0 0 100 100"
//         preserveAspectRatio="none"
//       >
//         {/* 하단 중앙에서 시작해서 왼쪽으로 돌고 상단 중앙에서 끝나는 경로 */}
//         <path
//           d="M 50,99 L 1,99 L 1,1 L 50,1"
//           fill="none"
//           stroke="#00ffe1"
//           strokeWidth="1.5"
//           pathLength="1"
//           strokeDasharray="0.025 0.975"
//           strokeDashoffset="1"
//           className="glow-one"
//         />

//         {/* 하단 중앙에서 시작해서 오른쪽으로 돌고 상단 중앙에서 끝나는 경로 */}
//         <path
//           d="M 50,99 L 99,99 L 99,1 L 50,1"
//           fill="none"
//           stroke="#00ffe1"
//           strokeWidth="1.5"
//           pathLength="1"
//           strokeDasharray="0.025 0.975"
//           strokeDashoffset="1"
//           className="glow-two"
//         />
//       </svg>
//       <div className="z-10 font-bold text-white">LOGO</div>
//     </div>
//   )
// }

import React from 'react';

export default function NeonTwinGlowBox() {
  return (
    <div className="relative flex h-12 w-48 items-center justify-center rounded-md bg-black">
      <svg
        className="pointer-events-none absolute top-0 left-0 h-full w-full"
        viewBox="0 0 100 100"
        preserveAspectRatio="none"
      >
        {/* 하단 중앙에서 시작해서 왼쪽으로 돌고 상단 중앙에서 끝나는 경로 */}
        <path
          d="M 50,99 L 1,99 L 1,1 L 50,1"
          fill="none"
          stroke="#00ffe1"
          strokeWidth="1.5"
          pathLength="1" 
          strokeDasharray="0.025 0.975"
          strokeDashoffset="1"
          className="glow-one"
        />

        {/* 하단 중앙에서 시작해서 오른쪽으로 돌고 상단 중앙에서 끝나는 경로 */}
        <path
          d="M 50,99 L 99,99 L 99,1 L 50,1"
          fill="none"
          stroke="#00ffe1"
          strokeWidth="1.5"
          pathLength="1"
          strokeDasharray="0.025 0.975"
          strokeDashoffset="1"
          className="glow-two"
        />
      </svg>
      <div className="z-10 font-bold text-white">LOGO</div>
    </div>
  )
}