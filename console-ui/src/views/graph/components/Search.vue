<template>
  <div class="graph-search">
    <el-autocomplete
        v-model="keyword"
        :fetch-suggestions="querySearch"
        :trigger-on-focus="false"
        clearable
        class="inline-input"
        placeholder="请输入接口或服务名称"
        @select="handleSelect"
    />
    <el-button
        class="button"
        @click="clean">
      重置
    </el-button>
  </div>
</template>
<style lang="scss">
  .graph-search {
    padding: 5px;
    display: flex;
    flex-flow: row nowrap;
    justify-content: space-between;
    align-items: center;
  }
  .inline-input {
    margin: 0 5px;
    width: 180px;
  }
</style>
<script setup lang="ts">
import 'element-plus/theme-chalk/src/message.scss'
import {defineEmits, defineExpose, ref} from "vue";
import {OptionDataItem} from "echarts/types/src/util/types";
import {GraphNodeItemOption} from "echarts/types/dist/shared";

const emit = defineEmits(['openHighlightEvent', 'closeHighlightEvent'])
const keyword = ref('')
let restaurants;
const setSearchList = (value: string) => (restaurants = value)
defineExpose({setSearchList})

const querySearch = (queryString: string, cb: any) => {
  const results = queryString
      ? restaurants.filter(createFilter(queryString))
      : restaurants
  // call callback function to return suggestions
  cb(results)
}

const createFilter = (queryString: string) => {
  return (restaurant: OptionDataItem) => {
    return (
        restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    )
  }
}

const handleSelect = (item: GraphNodeItemOption) => {
  emit('openHighlightEvent', item.value)
}

const clean = () => {
  keyword.value = ''
  emit('closeHighlightEvent')
}
</script>
