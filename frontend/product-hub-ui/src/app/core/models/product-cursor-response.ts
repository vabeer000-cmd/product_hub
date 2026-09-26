import { Product } from "./product.model";

export interface ProductCursorResponse {
  content: Product[];
  nextCursor: string | null;
  hasNext: boolean;
}