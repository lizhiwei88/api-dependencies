<template>
  <router-view/>
</template>

<style lang="scss">
/*全局去除默认样式*/
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
  font-size: 14px;
}

/*a标签去除默认样式*/
a {
  text-decoration: none;
  color: #333;
}

// 设置使所有a标签的四种状态都和本身颜色保持一致
a:hover, a:visited, a:link, a:active {
  color: #333;
}

/*li*/
ul, ol {
  list-style: none;
}

/*img*/
img {
  vertical-align: top;
  border: none;
}

/*button*/
.button {
  border: 0;
  background-color: none;
  outline: none;
  -webkit-appearance: none; //用于IOS下移除原生样式
}

/*select*/
select {
  border: none;
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;
  /*在选择框的最右侧中间显示小箭头图片*/
  background: url("http://ourjs.github.io/static/2015/arrow.png") no-repeat scroll right center transparent;
  padding-right: 14px;
}

/*h1*/
h1, h2, h3, h4, h5, h6 {
  font-weight: normal;
}

/*input，textarea*/
input, textarea {
  outline: none;
  border: none;
}

textarea {
  resize: none;
  overflow: auto;
}

/*斜体 i*/
i {
  font-style: normal;
}

/*table*/
table {
  border-collapse: collapse;
  border-spacing: 0;
}

/*去除input[type=number]加减号*/
/* 谷歌 */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  appearance: none;
  margin: 0;
}

/* 火狐 */
input {
  -moz-appearance: textfield;
}
</style>
