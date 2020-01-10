webpackJsonp([10],{EUih:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var l=a("mvHQ"),o=a.n(l),r=a("P9l9"),n={name:"AddLayernode",data:function(){var e=function(e,t,a){t&&!/^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{4}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/.test(t)?a(new Error("端口格式错误")):a()};return{activeTabName:"AddLayernode",typeOptions:[{label:"JV230接入",value:"ACCESS_JV230"},{label:"机顶盒接入",value:"ACCESS_TVOS"},{label:"混合器接入",value:"ACCESS_MIXER"},{label:"手机接入",value:"ACCESS_MOBILE"},{label:"音视频转发服务设备",value:"ACCESS_JV210"},{label:"录像存储服务单元A型",value:"ACCESS_CDN"},{label:"录像存储服务单元B型",value:"ACCESS_VOD"},{label:"监控资源汇接网关",value:"ACCESS_IPC"},{label:"流媒体管理服务设备",value:"ACCESS_STREAMMEDIA"},{label:"联网服务设备",value:"ACCESS_NETWORK"},{label:"显控汇接网关",value:"ACCESS_DISPLAYCTRL"},{label:"流转发器接入",value:"ACCESS_S100"},{label:"点播代理服务设备",value:"ACCESS_VODPROXY"}],nodeForm:{type:"ACCESS_JV210",name:"",ip:"",nodeUid:"",port:null,downloadPort:null},rules:{type:[{required:!0,message:"请选择节点类型",trigger:"change"}],name:[{required:!0,message:"请输入节点名称",trigger:"change"},{min:1,max:30,message:"长度在 1 到 30 个字符",trigger:"blur"}],nodeUid:[{required:!0,message:"请输入节点ID",trigger:"change"},{min:1,max:30,message:"长度在 1 到 30 个字符",trigger:"blur"}],ip:[{validator:function(e,t,a){t&&!/^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/.test(t)?a(new Error("IP格式错误")):a()},trigger:"blur"}],port:[{validator:e,trigger:"blur"}],downloadPort:[{validator:e,trigger:"blur"}]}}},methods:{handleTabClick:function(e,t){"AddLayernode"!==e.name&&this.$router.push("/"+e.name)},reset:function(){this.$refs.nodeForm.resetFields()},submit:function(){var e=this;if(this.validate()){var t={json:o()(this.nodeForm)};Object(r.X)(t).then(function(t){t.errMsg?e.$message({message:t.errMsg,type:"error"}):(e.$message({message:"添加成功",type:"success"}),e.$refs.nodeForm.resetFields())})}},validate:function(){var e=!1;return this.$refs.nodeForm.validate(function(t){e=t}),e}},mounted:function(){}},i={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",[a("el-tabs",{staticStyle:{float:"left",width:"100%"},on:{"tab-click":e.handleTabClick},model:{value:e.activeTabName,callback:function(t){e.activeTabName=t},expression:"activeTabName"}},[a("el-tab-pane",{attrs:{label:"层节点列表",name:"LayernodeManage"}}),e._v(" "),a("el-tab-pane",{attrs:{label:"添加层节点",name:"AddLayernode"}})],1),e._v(" "),a("el-form",{ref:"nodeForm",attrs:{model:e.nodeForm,rules:e.rules,"label-width":"100px"}},[a("el-form-item",{attrs:{label:"节点类型",prop:"type"}},[a("el-select",{staticStyle:{width:"200px"},model:{value:e.nodeForm.type,callback:function(t){e.$set(e.nodeForm,"type",t)},expression:"nodeForm.type"}},e._l(e.typeOptions,function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"节点名称",prop:"name"}},[a("el-input",{staticStyle:{width:"200px"},model:{value:e.nodeForm.name,callback:function(t){e.$set(e.nodeForm,"name",t)},expression:"nodeForm.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"节点ID",prop:"nodeUid"}},[a("el-input",{staticStyle:{width:"200px"},model:{value:e.nodeForm.nodeUid,callback:function(t){e.$set(e.nodeForm,"nodeUid",t)},expression:"nodeForm.nodeUid"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"节点IP",prop:"ip"}},[a("el-input",{staticStyle:{width:"200px"},model:{value:e.nodeForm.ip,callback:function(t){e.$set(e.nodeForm,"ip",t)},expression:"nodeForm.ip"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"节点端口",prop:"port"}},[a("el-input",{staticStyle:{width:"200px"},model:{value:e.nodeForm.port,callback:function(t){e.$set(e.nodeForm,"port",t)},expression:"nodeForm.port"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"下载端口",prop:"downloadPort"}},[a("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"录制资源下载端口，可不填"},model:{value:e.nodeForm.downloadPort,callback:function(t){e.$set(e.nodeForm,"downloadPort",t)},expression:"nodeForm.downloadPort"}})],1),e._v(" "),a("div",{staticStyle:{"margin-top":"30px"}},[a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.submit()}}},[e._v("提交")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.reset()}}},[e._v("重置")])],1)],1)],1)},staticRenderFns:[]};var d=a("VU/8")(n,i,!1,function(e){a("rJN3")},"data-v-39d2fac0",null);t.default=d.exports},rJN3:function(e,t){}});
//# sourceMappingURL=10.90c609cf3c97020c7742.js.map