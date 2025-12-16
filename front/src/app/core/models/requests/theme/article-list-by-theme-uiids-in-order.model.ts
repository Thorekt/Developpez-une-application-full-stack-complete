import { OrderType } from "src/app/core/type/order.type";

/**
 * ArticleListByThemeUuidsInOrder represents a request to get articles filtered by theme UUIDs and sorted by order.
 * 
 * @author Thorekt
 */
export interface ArticleListByThemeUuidsInOrder {
    themeUuids: string[];
    order: OrderType;
}