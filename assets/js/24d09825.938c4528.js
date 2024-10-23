"use strict";(self.webpackChunkweb_docs=self.webpackChunkweb_docs||[]).push([[796],{3905:(e,t,n)=>{n.d(t,{Zo:()=>u,kt:()=>m});var r=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function a(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,o=function(e,t){if(null==e)return{};var n,r,o={},i=Object.keys(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var s=r.createContext({}),c=function(e){var t=r.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):a(a({},t),e)),n},u=function(e){var t=c(e.components);return r.createElement(s.Provider,{value:t},e.children)},p="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},f=r.forwardRef((function(e,t){var n=e.components,o=e.mdxType,i=e.originalType,s=e.parentName,u=l(e,["components","mdxType","originalType","parentName"]),p=c(n),f=o,m=p["".concat(s,".").concat(f)]||p[f]||d[f]||i;return n?r.createElement(m,a(a({ref:t},u),{},{components:n})):r.createElement(m,a({ref:t},u))}));function m(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var i=n.length,a=new Array(i);a[0]=f;var l={};for(var s in t)hasOwnProperty.call(t,s)&&(l[s]=t[s]);l.originalType=e,l[p]="string"==typeof e?e:o,a[1]=l;for(var c=2;c<i;c++)a[c]=n[c];return r.createElement.apply(null,a)}return r.createElement.apply(null,n)}f.displayName="MDXCreateElement"},2552:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>c,contentTitle:()=>l,default:()=>f,frontMatter:()=>a,metadata:()=>s,toc:()=>u});var r=n(7462),o=(n(7294),n(3905)),i=n(6967);const a={id:"introduction",title:"Introduction",sidebar_label:"Introduction",slug:"/"},l=void 0,s={unversionedId:"introduction",id:"introduction",title:"Introduction",description:"Description",source:"@site/docs/01-INTRODUCTION.mdx",sourceDirName:".",slug:"/",permalink:"/sc-ewon-flexy-extensions-lib/docs/",draft:!1,editUrl:"https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/docs/01-INTRODUCTION.mdx",tags:[],version:"current",sidebarPosition:1,frontMatter:{id:"introduction",title:"Introduction",sidebar_label:"Introduction",slug:"/"},sidebar:"defaultSidebar",next:{title:"Change Log",permalink:"/sc-ewon-flexy-extensions-lib/docs/change-log"}},c={},u=[{value:"Description",id:"description",level:2},{value:"Getting Started",id:"getting-started",level:2},{value:"Features",id:"features",level:2},{value:"License",id:"license",level:2}],p={toc:u},d="wrapper";function f(e){let{components:t,...n}=e;return(0,o.kt)(d,(0,r.Z)({},p,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h2",{id:"description"},"Description"),(0,o.kt)("p",null,"A library of extensions to the functionality of the Ewon Flexy Java environment (Ewon ETK), created\nby the HMS Networks, MU Americas Solution Center."),(0,o.kt)("h2",{id:"getting-started"},"Getting Started"),(0,o.kt)("p",null,"To get started, please refer to the ",(0,o.kt)("a",{parentName:"p",href:"/sc-ewon-flexy-extensions-lib/docs/quick-start-guide"},"Quick Start Guide"),". This guide\nprovides instructions for using the library, and how to access it via the Solution Center's\nMaven repository."),(0,o.kt)("h2",{id:"features"},"Features"),(0,o.kt)(i.ZP,{mdxType:"FeaturesPartial"}),(0,o.kt)("h2",{id:"license"},"License"),(0,o.kt)("p",null,"This project is licensed under the terms of the Apache 2.0 license. More information about the\nlicense and the full text of the license, can be found on the ",(0,o.kt)("a",{parentName:"p",href:"/sc-ewon-flexy-extensions-lib/docs/legal/license"},"LICENSE"),"\npage."))}f.isMDXComponent=!0},6967:(e,t,n)=>{n.d(t,{ZP:()=>l});var r=n(7462),o=(n(7294),n(3905));const i={toc:[{value:"Expands upon the Ewon Flexy&#39;s Java API",id:"expands-upon-the-ewon-flexys-java-api",level:3},{value:"Flexible usage from simple to complex",id:"flexible-usage-from-simple-to-complex",level:3},{value:"Easy to use",id:"easy-to-use",level:3}]},a="wrapper";function l(e){let{components:t,...n}=e;return(0,o.kt)(a,(0,r.Z)({},i,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h3",{id:"expands-upon-the-ewon-flexys-java-api"},"Expands upon the Ewon Flexy's Java API"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"Improves flexibility of standard Ewon functionality"),(0,o.kt)("li",{parentName:"ul"},"Adds packages which expand functionality of the Ewon")),(0,o.kt)("h3",{id:"flexible-usage-from-simple-to-complex"},"Flexible usage from simple to complex"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"Features are designed to be used in a variety of ways"),(0,o.kt)("li",{parentName:"ul"},"Utilities are provided to make common tasks easier"),(0,o.kt)("li",{parentName:"ul"},"Classes are designed to be extensible")),(0,o.kt)("h3",{id:"easy-to-use"},"Easy to use"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"Simply add the dependency to your Maven project and start using it"),(0,o.kt)("li",{parentName:"ul"},"No additional steps required")))}l.isMDXComponent=!0}}]);