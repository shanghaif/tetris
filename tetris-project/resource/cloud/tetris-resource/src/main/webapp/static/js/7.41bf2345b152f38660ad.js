webpackJsonp([7],{F8Gw:function(e,t){},qoXU:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var l=a("mvHQ"),s=a.n(l),n=a("BO1k"),i=a.n(n),r=a("P9l9"),o={name:"templateManage",data:function(){return{activeTabName:"BundleTemplateManage",filters:{keyword:""},tableLoading:!1,templates:[],multipleSelection:[],templateTree:[],treeProps:{children:"children",label:"label"},uploadUrl:"https://__requestIP__:8743/suma-venus-resource/template/import"}},methods:{handleTabClick:function(e,t){"BundleTemplateManage"!==e.name&&this.$router.push("/"+e.name)},getTemplates:function(){var e=this;this.tableLoading=!0;var t={name:this.filters.keyword};Object(r.D)(t).then(function(t){t.errMsg?e.$message({message:t.errMsg,type:"error"}):e.templates=t.templates,e.tableLoading=!1})},handleSelectionChange:function(e){this.multipleSelection=e},handleDetail:function(e){var t=this,a={channelTemplateId:e.id};Object(r.C)(a).then(function(e){e.errMsg?t.$message({message:e.errMsg,type:"error"}):t.templateTree=e.tree})},handleSingleDelete:function(e){var t=this;this.$confirm("是否确认删除?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){var a={templateIds:e.id};Object(r.q)(a).then(function(e){e.errMsg?t.$message({message:e.errMsg,type:"error"}):(t.$message({message:"删除成功",type:"success"}),t.getTemplates(),t.templateTree=[])})}).catch(function(){t.$message({type:"info",message:"已取消删除"})})},handleMultiDelete:function(){var e=this;this.multipleSelection.length?this.$confirm("是否确认删除所有选定行?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){var t="",a=!0,l=!1,s=void 0;try{for(var n,o=i()(e.multipleSelection);!(a=(n=o.next()).done);a=!0){t+=n.value.id,t+=";"}}catch(e){l=!0,s=e}finally{try{!a&&o.return&&o.return()}finally{if(l)throw s}}var c={templateIds:t};Object(r.q)(c).then(function(t){t.errMsg?e.$message({message:t.errMsg,type:"error"}):(e.$message({message:"删除成功",type:"success"}),e.getTemplates(),e.templateTree=[])})}).catch(function(){e.$message({type:"info",message:"已取消删除"})}):this.$message({message:"请至少选择一行",type:"warning"})},uploadSuccess:function(e){e.errMsg?this.$message({message:e.errMsg,type:"error"}):(this.$message({message:"导入成功",type:"success"}),this.getTemplates())},uploadError:function(e,t,a){this.$message({message:"上传文件错误"+s()(e),type:"error"})}},mounted:function(){if(this.getTemplates(),-1!==this.uploadUrl.indexOf("__requestIP__")){var e=document.location.host.split(":")[0];this.uploadUrl=this.uploadUrl.replace("__requestIP__",e)}}},c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",[a("el-tabs",{staticStyle:{float:"left",width:"100%"},on:{"tab-click":e.handleTabClick},model:{value:e.activeTabName,callback:function(t){e.activeTabName=t},expression:"activeTabName"}},[a("el-tab-pane",{attrs:{label:"设备模板管理",name:"BundleTemplateManage"}})],1),e._v(" "),a("el-card",{staticStyle:{float:"left","margin-top":"10px",width:"50%","min-height":"700px"},attrs:{"body-style":"padding:0px"}},[a("div",{staticStyle:{height:"30px"},attrs:{slot:"header"},slot:"header"},[a("el-input",{staticStyle:{float:"left",width:"150px"},attrs:{size:"small",placeholder:"关键字"},model:{value:e.filters.keyword,callback:function(t){e.$set(e.filters,"keyword",t)},expression:"filters.keyword"}}),e._v(" "),a("el-button",{staticStyle:{float:"left","margin-left":"30px"},attrs:{size:"small",type:"info"},on:{click:e.getTemplates}},[e._v("查询")]),e._v(" "),a("el-upload",{staticClass:"upload-demo",staticStyle:{float:"right","margin-right":"10px"},attrs:{action:e.uploadUrl,name:"filePoster","show-file-list":!1,"on-success":e.uploadSuccess,"on-error":e.uploadError}},[a("el-button",{attrs:{size:"small",type:"primary"}},[e._v("导入模板")])],1),e._v(" "),a("el-button",{staticStyle:{float:"right","margin-right":"10px"},attrs:{size:"small",type:"danger"},on:{click:e.handleMultiDelete}},[e._v("批量删除")])],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.tableLoading,expression:"tableLoading"}],staticStyle:{width:"100%"},attrs:{data:e.templates},on:{"selection-change":e.handleSelectionChange}},[a("el-table-column",{attrs:{width:"100",type:"selection"}}),e._v(" "),a("el-table-column",{attrs:{prop:"deviceModel",label:"设备类型",width:"300",sortable:""}}),e._v(" "),a("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.handleDetail(t.row)}}},[e._v("详情")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.handleSingleDelete(t.row)}}},[e._v("删除")])]}}])})],1)],1),e._v(" "),a("el-card",{staticStyle:{float:"left","margin-left":"50px","margin-top":"10px",width:"40%","min-height":"700px"},attrs:{"body-style":"padding:0px"}},[a("div",{staticStyle:{height:"30px"},attrs:{slot:"header"},slot:"header"},[a("span",{staticStyle:{float:"left","font-size":"18px"}},[e._v("模板属性")])]),e._v(" "),a("el-tree",{attrs:{data:e.templateTree,props:e.treeProps,"default-expand-all":""}})],1)],1)},staticRenderFns:[]};var p=a("VU/8")(o,c,!1,function(e){a("F8Gw")},"data-v-6279f9bb",null);t.default=p.exports}});
//# sourceMappingURL=7.41bf2345b152f38660ad.js.map