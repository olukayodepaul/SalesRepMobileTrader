package com.mobbile.paul.ui.orders

sealed class OrdersEntryAdapter{

    class MultiSelectionState: OrdersEntryAdapter() {
        override fun toString(): String {
            return "MultiSelectionState"
        }
    }

    class SearchViewState: OrdersEntryAdapter() {
        override fun toString(): String {
            return "SearchViewState"
        }
    }
}