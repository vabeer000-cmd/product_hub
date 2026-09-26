import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCursorList } from './product-cursor-list';

describe('ProductCursorList', () => {
  let component: ProductCursorList;
  let fixture: ComponentFixture<ProductCursorList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductCursorList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductCursorList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
