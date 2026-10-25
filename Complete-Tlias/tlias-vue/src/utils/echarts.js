// 按需注册echarts
// 1. 引入核心接口
import * as echarts from 'echarts/core';

// 2. 引入基础组件
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';

import { BarChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart } from 'echarts/charts';
import { LabelLayout } from 'echarts/features';


// 3. 注册引入的东西
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  BarChart,
  CanvasRenderer,
  PieChart,
  LabelLayout
]);

export  {echarts}