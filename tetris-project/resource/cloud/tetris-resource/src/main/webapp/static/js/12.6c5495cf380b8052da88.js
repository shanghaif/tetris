webpackJsonp([12],{"4vq3":function(e,r,t){"use strict";Object.defineProperty(r,"__esModule",{value:!0});var s=t("BO1k"),a=t.n(s),i=t("nvbp"),l=t.n(i),h=t("P9l9"),n={data:function(){return{activeTabName:"BindResource",roles:[],currentRoleRow:null,resources:[],bindTypeOptions:[{value:"all",label:"全部"},{value:"binded",label:"已绑定"},{value:"unbinded",label:"未绑定"}],deviceModelOptions:[],filters:{deviceModel:"",keyword:"",bindType:"all",countPerPage:""},total:0,pageNum:1,countPerPage:20,roleTableLoading:!1,resourceTableLoading:!1,prevReadChecks:[],prevWriteChecks:[],prevHJChecks:[],readChecks:[],writeChecks:[],HJchecks:[],detailDialogVisible:!1,detailInfos:[],users:[],userTableLoading:!1,resourceTableShow:!0,userTableShow:!1,checkReadAll:!1,checkWriteAll:!1,checkHJAll:!1}},methods:{handleTabClick:function(e,r){"BindResource"!==e.name&&this.$router.push("/"+e.name)},handleRoleTableRowChange:function(e){this.currentRoleRow=e,this.resourceTableShow?this.getResources():this.userTableShow&&this.getUsers()},renderCheckReadHeader:function(e,r){var t=this;return e("el-checkbox",l()([{attrs:{value:t.checkReadAll},on:{input:function(e){t.checkReadAll=e}}},{directives:[{name:"model",value:t.checkReadAll}]},{on:{change:this.handleCheckReadAllChange}}]),["录制"])},renderCheckWriteHeader:function(e,r){var t=this;return e("el-checkbox",l()([{attrs:{value:t.checkWriteAll},on:{input:function(e){t.checkWriteAll=e}}},{directives:[{name:"model",value:t.checkWriteAll}]},{on:{change:this.handleCheckWriteAllChange}}]),["点播"])},renderCheckHJHeader:function(e,r){var t=this;return e("el-checkbox",l()([{attrs:{value:t.checkHJAll},on:{input:function(e){t.checkHJAll=e}}},{directives:[{name:"model",value:t.checkHJAll}]},{on:{change:this.handleCheckHJAllChange}}]),["呼叫"])},handleCheckReadAllChange:function(e){if(this.resourceTableShow)if(e){var r=!0,t=!1,s=void 0;try{for(var i,l=a()(this.resources);!(r=(i=l.next()).done);r=!0){var h=i.value;this.readChecks.push(h.bundleId),h.hasReadPrivilege=!0}}catch(e){t=!0,s=e}finally{try{!r&&l.return&&l.return()}finally{if(t)throw s}}}else{this.readChecks=[];var n=!0,c=!1,o=void 0;try{for(var u,d=a()(this.resources);!(n=(u=d.next()).done);n=!0){u.value.hasReadPrivilege=!1}}catch(e){c=!0,o=e}finally{try{!n&&d.return&&d.return()}finally{if(c)throw o}}}else if(this.userTableShow)if(e){var v=!0,g=!1,k=void 0;try{for(var f,p=a()(this.users);!(v=(f=p.next()).done);v=!0){var b=f.value;this.readChecks.push(b.userNo),b.hasReadPrivilege=!0}}catch(e){g=!0,k=e}finally{try{!v&&p.return&&p.return()}finally{if(g)throw k}}}else{this.readChecks=[];var w=!0,C=!1,y=void 0;try{for(var m,P=a()(this.users);!(w=(m=P.next()).done);w=!0){m.value.hasReadPrivilege=!1}}catch(e){C=!0,y=e}finally{try{!w&&P.return&&P.return()}finally{if(C)throw y}}}},handleCheckWriteAllChange:function(e){if(this.resourceTableShow)if(e){var r=!0,t=!1,s=void 0;try{for(var i,l=a()(this.resources);!(r=(i=l.next()).done);r=!0){var h=i.value;this.writeChecks.push(h.bundleId),h.hasWritePrivilege=!0}}catch(e){t=!0,s=e}finally{try{!r&&l.return&&l.return()}finally{if(t)throw s}}}else{this.writeChecks=[];var n=!0,c=!1,o=void 0;try{for(var u,d=a()(this.resources);!(n=(u=d.next()).done);n=!0){u.value.hasWritePrivilege=!1}}catch(e){c=!0,o=e}finally{try{!n&&d.return&&d.return()}finally{if(c)throw o}}}else if(this.userTableShow)if(e){var v=!0,g=!1,k=void 0;try{for(var f,p=a()(this.users);!(v=(f=p.next()).done);v=!0){var b=f.value;this.writeChecks.push(b.userNo),b.hasWritePrivilege=!0}}catch(e){g=!0,k=e}finally{try{!v&&p.return&&p.return()}finally{if(g)throw k}}}else{this.writeChecks=[];var w=!0,C=!1,y=void 0;try{for(var m,P=a()(this.users);!(w=(m=P.next()).done);w=!0){m.value.hasWritePrivilege=!1}}catch(e){C=!0,y=e}finally{try{!w&&P.return&&P.return()}finally{if(C)throw y}}}},handleCheckHJAllChange:function(e){if(this.userTableShow)if(e){var r=!0,t=!1,s=void 0;try{for(var i,l=a()(this.users);!(r=(i=l.next()).done);r=!0){var h=i.value;this.HJchecks.push(h.userNo),h.hasHJPrivilege=!0}}catch(e){t=!0,s=e}finally{try{!r&&l.return&&l.return()}finally{if(t)throw s}}}else{this.HJchecks=[];var n=!0,c=!1,o=void 0;try{for(var u,d=a()(this.users);!(n=(u=d.next()).done);n=!0){u.value.hasHJPrivilege=!1}}catch(e){c=!0,o=e}finally{try{!n&&d.return&&d.return()}finally{if(c)throw o}}}},handleCheckReadChange:function(e){this.resourceTableShow?e.hasReadPrivilege?(this.readChecks.push(e.bundleId),this.readChecks.length===this.resources.length&&(this.checkReadAll=!0)):(this.readChecks.splice(this.readChecks.indexOf(e.bundleId),1),this.checkReadAll=!1):this.userTableShow&&(e.hasReadPrivilege?(this.readChecks.push(e.userNo),this.readChecks.length===this.users.length&&(this.checkReadAll=!0)):(this.readChecks.splice(this.readChecks.indexOf(e.userNo),1),this.checkReadAll=!1))},handleCheckWriteChange:function(e){this.resourceTableShow?e.hasWritePrivilege?(this.writeChecks.push(e.bundleId),this.writeChecks.length===this.resources.length&&(this.checkWriteAll=!0)):(this.writeChecks.splice(this.writeChecks.indexOf(e.bundleId),1),this.checkWriteAll=!1):this.userTableShow&&(e.hasWritePrivilege?(this.writeChecks.push(e.userNo),this.writeChecks.length===this.users.length&&(this.checkWriteAll=!0)):(this.writeChecks.splice(this.writeChecks.indexOf(e.userNo),1),this.checkWriteAll=!1))},handleCheckHJChange:function(e){this.userTableShow&&(e.hasHJPrivilege?(this.HJchecks.push(e.userNo),this.HJchecks.length===this.users.length&&(this.checkHJAll=!0)):(this.HJchecks.splice(this.HJchecks.indexOf(e.userNo),1),this.checkHJAll=!1))},getRoles:function(){var e=this;this.roleTableLoading=!0,Object(h.r)({}).then(function(r){r.errMsg?e.$message({message:r.errMsg,type:"error"}):e.roles=r.roles,e.roleTableLoading=!1})},getDeviceModels:function(){var e=this;Object(h.y)().then(function(r){if(!r.errMsg&&r.deviceModels){e.deviceModelOptions.push({value:"",label:"全部类型"});var t=!0,s=!1,i=void 0;try{for(var l,h=a()(r.deviceModels);!(t=(l=h.next()).done);t=!0){var n=l.value,c={value:n,label:n};e.deviceModelOptions.push(c)}}catch(e){s=!0,i=e}finally{try{!t&&h.return&&h.return()}finally{if(s)throw i}}}})},getResources:function(){var e=this;if(this.currentRoleRow){this.resourceTableShow=!0,this.userTableShow=!1,this.prevReadChecks=[],this.prevWriteChecks=[],this.readChecks=[],this.writeChecks=[],this.checkReadAll=!1,this.checkWriteAll=!1,this.checkHJAll=!1,this.countPerPage=20,/^[1-9]+[0-9]*]*$/.test(this.filters.countPerPage)&&(this.countPerPage=parseInt(this.filters.countPerPage));var r={roleId:this.currentRoleRow.id,bindType:this.filters.bindType,deviceModel:this.filters.deviceModel,keyword:this.filters.keyword,pageNum:this.pageNum,countPerPage:this.countPerPage};this.resourceTableLoading=!0,Object(h.x)(r).then(function(r){if(r.errMsg)e.$message({message:r.errMsg,type:"error"});else{e.total=r.total,e.resources=r.resources;var t=!0,s=!1,i=void 0;try{for(var l,h=a()(e.resources);!(t=(l=h.next()).done);t=!0){var n=l.value;n.hasReadPrivilege&&(e.prevReadChecks.push(n.bundleId),e.readChecks.push(n.bundleId)),n.hasWritePrivilege&&(e.prevWriteChecks.push(n.bundleId),e.writeChecks.push(n.bundleId))}}catch(e){s=!0,i=e}finally{try{!t&&h.return&&h.return()}finally{if(s)throw i}}e.readChecks.length===e.resources.length&&(e.checkReadAll=!0),e.writeChecks.length===e.resources.length&&(e.checkWriteAll=!0)}e.resourceTableLoading=!1})}else this.$message({message:"请先选择角色",type:"error"})},handleCurrentPageChange:function(e){this.pageNum=e,this.resourceTableShow?this.getResources():this.getUsers()},getUsers:function(){var e=this;if(this.currentRoleRow){this.resourceTableShow=!1,this.userTableShow=!0,this.prevReadChecks=[],this.prevWriteChecks=[],this.readChecks=[],this.writeChecks=[],this.prevHJChecks=[],this.HJchecks=[],this.checkReadAll=!1,this.checkWriteAll=!1,this.checkHJAll=!1,this.countPerPage=20,/^[1-9]+[0-9]*]*$/.test(this.filters.countPerPage)&&(this.countPerPage=parseInt(this.filters.countPerPage));var r={roleId:this.currentRoleRow.id,keyword:this.filters.keyword,pageNum:this.pageNum,countPerPage:this.countPerPage};this.userTableLoading=!0,Object(h.E)(r).then(function(r){if(r.errMsg)e.$message({message:r.errMsg,type:"error"});else{e.total=r.total,e.users=r.users;var t=!0,s=!1,i=void 0;try{for(var l,h=a()(e.users);!(t=(l=h.next()).done);t=!0){var n=l.value;n.hasReadPrivilege&&(e.prevReadChecks.push(n.userNo),e.readChecks.push(n.userNo)),n.hasWritePrivilege&&(e.prevWriteChecks.push(n.userNo),e.writeChecks.push(n.userNo)),n.hasHJPrivilege&&(e.prevHJChecks.push(n.userNo),e.HJchecks.push(n.userNo))}}catch(e){s=!0,i=e}finally{try{!t&&h.return&&h.return()}finally{if(s)throw i}}e.readChecks.length===e.users.length&&(e.checkReadAll=!0),e.writeChecks.length===e.users.length&&(e.checkWriteAll=!0),e.HJchecks.length===e.users.length&&(e.checkHJAll=!0)}e.userTableLoading=!1})}else this.$message({message:"请先选择角色",type:"error"})},submitPrivilege:function(){var e=this;if(this.resourceTableShow){var r={roleId:this.currentRoleRow.id,prevReadChecks:this.prevReadChecks.join(","),prevWriteChecks:this.prevWriteChecks.join(","),readChecks:this.readChecks.join(","),writeChecks:this.writeChecks.join(",")};this.resourceTableLoading=!0,Object(h._2)(r).then(function(r){r.errMsg?e.$message({message:r.errMsg,type:"error"}):(e.$message({message:"提交成功",type:"success"}),e.prevReadChecks=[].concat(e.readChecks),e.prevWriteChecks=[].concat(e.writeChecks)),e.resourceTableLoading=!1})}else if(this.userTableShow){var t={roleId:this.currentRoleRow.id,prevReadChecks:this.prevReadChecks.join(","),prevWriteChecks:this.prevWriteChecks.join(","),prevHJChecks:this.prevHJChecks.join(","),readChecks:this.readChecks.join(","),writeChecks:this.writeChecks.join(","),hjChecks:this.HJchecks.join(",")};this.userTableLoading=!0,Object(h._3)(t).then(function(r){r.errMsg?e.$message({message:r.errMsg,type:"error"}):(e.$message({message:"提交成功",type:"success"}),e.prevReadChecks=[].concat(e.readChecks),e.prevWriteChecks=[].concat(e.writeChecks),e.prevHJChecks=[].concat(e.HJchecks)),e.userTableLoading=!1})}}},mounted:function(){this.getDeviceModels(),this.getRoles()}},c={render:function(){var e=this,r=e.$createElement,t=e._self._c||r;return t("section",[t("el-tabs",{staticStyle:{float:"left",width:"100%"},on:{"tab-click":e.handleTabClick},model:{value:e.activeTabName,callback:function(r){e.activeTabName=r},expression:"activeTabName"}},[t("el-tab-pane",{attrs:{label:"绑定资源用户",name:"BindResource"}}),e._v(" "),t("el-tab-pane",{attrs:{label:"绑定虚拟资源",name:"BindVirtualResource"}})],1),e._v(" "),t("el-card",{staticStyle:{float:"left","margin-top":"10px",width:"20%","font-size":"18px"},attrs:{"body-style":"padding:0px"}},[t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.roleTableLoading,expression:"roleTableLoading"}],ref:"roleTable",staticStyle:{width:"100%"},attrs:{data:e.roles,"highlight-current-row":""},on:{"current-change":e.handleRoleTableRowChange}},[t("el-table-column",{attrs:{prop:"name",label:"角色名",sortable:""}})],1)],1),e._v(" "),t("el-card",{staticStyle:{float:"left","margin-left":"50px","margin-top":"10px",width:"70%"},attrs:{"body-style":"padding:0px"}},[t("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[t("el-input",{staticStyle:{float:"left","margin-left":"30px",width:"200px"},attrs:{size:"small",placeholder:"关键字"},model:{value:e.filters.keyword,callback:function(r){e.$set(e.filters,"keyword",r)},expression:"filters.keyword"}}),e._v(" "),t("el-button",{staticStyle:{float:"left","margin-left":"30px"},attrs:{size:"small",type:"info"},on:{click:e.getResources}},[e._v("查询设备")]),e._v(" "),t("el-button",{staticStyle:{float:"left","margin-left":"30px"},attrs:{size:"small",type:"info"},on:{click:e.getUsers}},[e._v("查询用户")]),e._v(" "),t("el-input",{staticStyle:{float:"right","margin-right":"30px",width:"200px"},attrs:{size:"small",placeholder:"单页显示数量,默认20"},model:{value:e.filters.countPerPage,callback:function(r){e.$set(e.filters,"countPerPage",r)},expression:"filters.countPerPage"}})],1),e._v(" "),t("el-table",{directives:[{name:"show",rawName:"v-show",value:e.resourceTableShow,expression:"resourceTableShow"},{name:"loading",rawName:"v-loading",value:e.resourceTableLoading,expression:"resourceTableLoading"}],staticStyle:{width:"100%"},attrs:{data:e.resources}},[t("el-table-column",{attrs:{type:"index",width:"100"}}),e._v(" "),t("el-table-column",{attrs:{prop:"name",label:"名称",width:"200",sortable:""}}),e._v(" "),t("el-table-column",{attrs:{prop:"deviceModel",label:"资源类型",width:"200",sortable:""}}),e._v(" "),t("el-table-column",{attrs:{prop:"username",label:"设备账号",width:"270"}}),e._v(" "),t("el-table-column",{attrs:{width:"150","render-header":e.renderCheckReadHeader},scopedSlots:e._u([{key:"default",fn:function(r){return[t("el-checkbox",{on:{change:function(t){e.handleCheckReadChange(r.row)}},model:{value:r.row.hasReadPrivilege,callback:function(t){e.$set(r.row,"hasReadPrivilege",t)},expression:"scope.row.hasReadPrivilege"}})]}}])}),e._v(" "),t("el-table-column",{attrs:{width:"150","render-header":e.renderCheckWriteHeader},scopedSlots:e._u([{key:"default",fn:function(r){return[t("el-checkbox",{on:{change:function(t){e.handleCheckWriteChange(r.row)}},model:{value:r.row.hasWritePrivilege,callback:function(t){e.$set(r.row,"hasWritePrivilege",t)},expression:"scope.row.hasWritePrivilege"}})]}}])})],1),e._v(" "),t("el-table",{directives:[{name:"show",rawName:"v-show",value:e.userTableShow,expression:"userTableShow"},{name:"loading",rawName:"v-loading",value:e.userTableLoading,expression:"userTableLoading"}],staticStyle:{width:"100%"},attrs:{data:e.users}},[t("el-table-column",{attrs:{type:"index",width:"100"}}),e._v(" "),t("el-table-column",{attrs:{prop:"name",label:"名称",width:"200",sortable:""}}),e._v(" "),t("el-table-column",{attrs:{prop:"userNo",label:"用户编号",width:"270"}}),e._v(" "),t("el-table-column",{attrs:{width:"150","render-header":e.renderCheckReadHeader},scopedSlots:e._u([{key:"default",fn:function(r){return[t("el-checkbox",{on:{change:function(t){e.handleCheckReadChange(r.row)}},model:{value:r.row.hasReadPrivilege,callback:function(t){e.$set(r.row,"hasReadPrivilege",t)},expression:"scope.row.hasReadPrivilege"}})]}}])}),e._v(" "),t("el-table-column",{attrs:{width:"150","render-header":e.renderCheckWriteHeader},scopedSlots:e._u([{key:"default",fn:function(r){return[t("el-checkbox",{on:{change:function(t){e.handleCheckWriteChange(r.row)}},model:{value:r.row.hasWritePrivilege,callback:function(t){e.$set(r.row,"hasWritePrivilege",t)},expression:"scope.row.hasWritePrivilege"}})]}}])}),e._v(" "),t("el-table-column",{attrs:{width:"150","render-header":e.renderCheckHJHeader},scopedSlots:e._u([{key:"default",fn:function(r){return[t("el-checkbox",{on:{change:function(t){e.handleCheckHJChange(r.row)}},model:{value:r.row.hasHJPrivilege,callback:function(t){e.$set(r.row,"hasHJPrivilege",t)},expression:"scope.row.hasHJPrivilege"}})]}}])})],1),e._v(" "),t("el-col",{staticClass:"toolbar",attrs:{span:24}},[t("el-button",{attrs:{type:"primary"},on:{click:e.submitPrivilege}},[e._v("提交")]),e._v(" "),t("el-pagination",{staticStyle:{float:"right"},attrs:{layout:"prev, pager, next","page-size":e.countPerPage,total:e.total},on:{"current-change":e.handleCurrentPageChange}})],1)],1)],1)},staticRenderFns:[]};var o=t("VU/8")(n,c,!1,function(e){t("OWO2")},null,null);r.default=o.exports},OWO2:function(e,r){}});
//# sourceMappingURL=12.6c5495cf380b8052da88.js.map