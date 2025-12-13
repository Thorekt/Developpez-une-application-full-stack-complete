import { OrderType } from "src/app/core/type/order.type";


export interface ArticleLlistByThemeUuidsInOrder {
    themeUuids: string[];
    order: OrderType;
}