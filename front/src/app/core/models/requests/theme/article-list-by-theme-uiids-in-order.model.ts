import { OrderType } from "src/app/core/type/order.type";


export interface ArticleListByThemeUuidsInOrder {
    themeUuids: string[];
    order: OrderType;
}