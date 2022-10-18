<template>
  <div class="page-top">
    <div class="logo">
      API Dependencies
    </div>
    <div class="github">
      <a href="https://github.com/lizhiwei88/api-dependencies" target="_blank" rel="noopener">
        <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" class="github-svg" viewBox="0 0 512 499.36" role="img"><title>GitHub</title><path fill="currentColor" fill-rule="evenodd" d="M256 0C114.64 0 0 114.61 0 256c0 113.09 73.34 209 175.08 242.9 12.8 2.35 17.47-5.56 17.47-12.34 0-6.08-.22-22.18-.35-43.54-71.2 15.49-86.2-34.34-86.2-34.34-11.64-29.57-28.42-37.45-28.42-37.45-23.27-15.84 1.73-15.55 1.73-15.55 25.69 1.81 39.21 26.38 39.21 26.38 22.84 39.12 59.92 27.82 74.5 21.27 2.33-16.54 8.94-27.82 16.25-34.22-56.84-6.43-116.6-28.43-116.6-126.49 0-27.95 10-50.8 26.35-68.69-2.63-6.48-11.42-32.5 2.51-67.75 0 0 21.49-6.88 70.4 26.24a242.65 242.65 0 0 1 128.18 0c48.87-33.13 70.33-26.24 70.33-26.24 14 35.25 5.18 61.27 2.55 67.75 16.41 17.9 26.31 40.75 26.31 68.69 0 98.35-59.85 120-116.88 126.32 9.19 7.9 17.38 23.53 17.38 47.41 0 34.22-.31 61.83-.31 70.23 0 6.85 4.61 14.81 17.6 12.31C438.72 464.97 512 369.08 512 256.02 512 114.62 397.37 0 256 0z"></path></svg>
      </a>
    </div>
  </div>
  <div class="graph-main">
    <Search ref="searchData"
            @openHighlightEvent="openHighlightEvent"
            @closeHighlightEvent="closeHighlightEvent">
    </Search>
    <div ref="graphView" :style="{ width: '100%', height: '800px' }"></div>
  </div>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import Search from "./components/Search";
import * as echarts from "echarts/core";
import {EChartsType} from "echarts/core";
import {
  LegendComponent,
  LegendComponentOption,
  TitleComponent,
  TitleComponentOption,
  TooltipComponent,
  TooltipComponentOption
} from "echarts/components";
import {GraphChart, GraphSeriesOption} from "echarts/charts";
import {CanvasRenderer} from "echarts/renderers";
import {GraphCategoryItemOption, GraphNodeStateOption} from "echarts/types/src/chart/graph/GraphSeries";
import {GraphEdgeItemObject, OptionDataValue} from "echarts/types/src/util/types";
import {CallbackDataParams, GraphNodeItemOption} from "echarts/types/dist/shared";
import {ElMessage} from "element-plus";
import {listDependencies} from "@/api/dependencies";

echarts.use(
    [TitleComponent, TooltipComponent, LegendComponent, GraphChart, CanvasRenderer]
)
type EChartsOption = echarts.ComposeOption<TitleComponentOption | TooltipComponentOption | LegendComponentOption | GraphSeriesOption>
let myChart: EChartsType
const graphView = ref()
const searchData = ref()

const buildOption: EChartsOption = (nodes : GraphNodeStateOption[], links: GraphEdgeItemObject<OptionDataValue>[], categories: GraphCategoryItemOption[]) => {
  nodes.forEach(function (nodeVO) {
    nodeVO.label = {
      show: true
    };
  })
  return {
    title: {
      text: '接口依赖关系图',
      subtext: 'Circular layout',
      top: 'bottom',
      left: 'right'
    },
    tooltip: {
      formatter: function (params: CallbackDataParams) {
        if (params.dataType == 'edge') {
          let source = nodes.find(item => item.id == params.data.source)
          let target = nodes.find(item => item.id == params.data.target)
          return source.api.method + ' ' + source.name
              + ' [' + categories[source.category].name + '] > '
              + target.name
        }
        let api: GraphNodeItemOption = params.data.api;
        if (api.type == 'service') {
          return 'name: ' + params.data.name + '</br>'
              + 'dependencies: ' + api.dependencies
        }

        if (api.type == 'api') {
          return 'service: ' + categories[params.data.category].name + '</br>'
              + 'name: ' + params.data.name + '</br>'
              + 'method: ' + api.method + '</br>'
              + 'dependencies: ' + api.dependencies + '</br>'
              + 'caller: ' + api.caller
        }
        return '';
      }
    },
    legend: [
      {
        top: 'bottom',
        data: categories.map(function (a) {
          return a.name;
        })
      }
    ],
    animationDurationUpdate: 1500,
    animationEasingUpdate: 'quinticInOut',
    series: [
      {
        name: '接口依赖关系图',
        type: 'graph',
        layout: 'circular',
        circular: {
          rotateLabel: true
        },
        data: nodes,
        links: links,
        categories: categories,
        roam: true,
        label: {
          position: 'right',
          formatter: function (params: CallbackDataParams) {
            let api: GraphNodeItemOption = params.data.api
            if (api.type == 'service') {
              return params.name
            }
            return api.method + ' ' + params.name
          }
        },
        lineStyle: {
          color: 'source',
          curveness: 0.3
        },
        emphasis: {
          focus: 'adjacency',
          lineStyle: {
            width: 10
          }
        }
      }
    ]
  }
}

const showGraphData = async () => {
  let { data } = await listDependencies()
  // let data = [
  //   {
  //     "service": "Order-Service",  // 使用接口的服务
  //     "apiDependenciesList": [
  //       {
  //         "targetService": "Goods-Service",  // 接口的服务
  //         "method": "GET",
  //         "uri": "/api/goods/1"
  //       },
  //       {
  //         "targetService": "Goods-Service",
  //         "method": "POST",
  //         "uri": "/api/goods/2"
  //       },
  //       {
  //         "targetService": "Goods-Service",
  //         "method": "DELETE",
  //         "uri": "/api/goods/3"
  //       }
  //     ]
  //   },
  //   {
  //     "service": "Goods-Service",
  //     "apiDependenciesList": [
  //       {
  //         "targetService": "Order-Service",
  //         "method": "GET",
  //         "uri": "/api/order/1"
  //       },
  //       {
  //         "targetService": "User-Service",
  //         "method": "POST",
  //         "uri": "/api/user/2"
  //       },
  //       {
  //         "targetService": "User-Service",
  //         "method": "DELETE",
  //         "uri": "/api/user/3"
  //       }
  //     ]
  //   },
  //   {
  //     "service": "User-Service",
  //     "apiDependenciesList": [
  //       {
  //         "targetService": "Order-Service",
  //         "method": "GET",
  //         "uri": "/api/order/1"
  //       },
  //       {
  //         "targetService": "Goods-Service",
  //         "method": "POST",
  //         "uri": "/api/goods/2"
  //       },
  //       {
  //         "targetService": "Goods-Service",
  //         "method": "DELETE",
  //         "uri": "/api/goods/3"
  //       }
  //     ]
  //   }
  // ]
  if (data.length == 0) {
    return
  }
  const graph = convertData(data);
  // 设置搜索栏
  searchData.value.setSearchList(graph.nodes.map(function (item) {
    return {value: item.name, nodeVO: item}
  }))
  let option = buildOption(graph.nodes, graph.links, graph.categories)
  myChart.setOption(option)
  myChart.on('dblclick', {
    seriesIndex: 0,
    componentType: 'series',
    dataType: 'nodeVO',
  }, function (params) {
    navigator.clipboard.writeText(params.data.name)
    ElMessage('name 复制成功')
  });
  myChart.hideLoading()
}

const addCategory = (categories, nodes, serviceName) => {
  if (!categories.find(item => item.name == serviceName)) {
    categories.push({
      name: serviceName
    })
    nodes.push({
      id: serviceName,
      name: serviceName,
      api: {
        type: "service",
        dependencies: 0
      },
      symbolSize: 50,
      category: categories.length - 1
    })
  }
}

const addNode = (categories, nodes, service) => {
  service.apiDependenciesList.map((api) => {
    if (!categories.find(item => item.name == api.targetService)) {
      categories.push({
        name: api.targetService
      })
      nodes.push({
        id: api.targetService,
        name: api.targetService,
        api: {
          type: "service",
          dependencies: 0
        },
        symbolSize: 50,
        category: categories.length - 1
      })
    }
    // 是否存在, 存在依赖+1
    let nodeItem = nodes.find((item) => item.id == api.targetService + '-' + api.method + '-' + api.uri)
    if (nodeItem) {
      nodeItem.api.dependencies++
      nodeItem.api.caller.push(service.service)
    } else {
      nodes.push({
        id: api.targetService + '-' + api.method + '-' + api.uri,
        name: api.uri,
        api: {
          type: "api",
          method: api.method,
          dependencies: 1,
          caller: [service.service]
        },
        symbolSize: 20,
        category: findCategoriesIndex(categories, api.targetService)
      });
    }
  })
}


const linksFunc = (data :GraphNodeItemOption[]) => {
  let links = [];
  data.forEach(node => {
    if (node.api.type === 'api') {
      node.api.caller.forEach(targetName => {
        links.push({
          source: node.id,
          target: targetName
        });
        // 计数
        data.find((item) => {
          if (item.id == targetName) {
            item.api.dependencies++
          }
        })
      })
    }
  });
  return links;
}

const convertData = (data: global.ApiDependencies) => {
  let categories = [];
  let nodes: GraphNodeItemOption[] = [];
  data.map(item => {
    // 加入分类
    addCategory(categories, nodes, item.service)
    // 加入Node
    addNode(categories, nodes, item)
  })
  return {
    nodes: nodes,
    links: linksFunc(nodes),
    categories: categories
  }
}

const findCategoriesIndex = (categories :GraphCategoryItemOption[], name :string) => {
  for (let i = 0; i < categories.length; i++) {
    if (categories[i].name === name) {
      return i
    }
  }
  return -1
}


onMounted(() => {
  myChart = echarts.init(graphView.value)
  myChart.showLoading()
  showGraphData()
  window.setInterval(showGraphData, 3000);
})

const openHighlightEvent = (value: string) => {
  myChart.dispatchAction({
    type: 'highlight',
    seriesIndex: 0,
    name: value,
  });
  // 关闭高亮
  myChart.setOption({
    series: [
      {
        emphasis: {
          disabled: true
        }
      }
    ]
  })
}

const closeHighlightEvent = () => {
  myChart.dispatchAction({
    type: 'downplay',
    seriesIndex: 0,
  });
  // 打开高亮/tool
  myChart.setOption({
    tooltip: {
      show: true,
    },
    series: [
      {
        emphasis: {
          disabled: false
        }
      }
    ]
  })
}
</script>
<style lang="scss">
.page-top{
  width: 100%;
  height: 64px;
  background-color: #24292f;
  margin-bottom: 5px;
  padding: 0 5px;
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;
  align-items: center;
  color: #ffffff;
}
.logo {
  font-size: 26px;
}
.github a {
  display: block;
  color: #ffffff;
}
.github-svg {
  display: inline-block;
}
.graph-main {
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
}
</style>
