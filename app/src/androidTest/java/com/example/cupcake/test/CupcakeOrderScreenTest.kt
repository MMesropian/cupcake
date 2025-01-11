package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {
    @get:Rule

    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeOrderUiState = OrderUiState(
        quantity = 6,
        flavor = "Vanilla",
        date = "Wed Jul 21",
        price = "$100",
        pickupOptions = listOf()
    )

    @Test
    fun summaryScreen_verifyContent() {
        composeTestRule.setContent {
            OrderSummaryScreen(fakeOrderUiState,
                { },
                { subject: String, summary: String -> }
            )
        }
        val numberCupcakes = composeTestRule.activity.resources.getQuantityString(
            R.plurals.cupcakes,
            fakeOrderUiState.quantity,
            fakeOrderUiState.quantity
        )
        composeTestRule.onNodeWithText(numberCupcakes)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeOrderUiState.flavor.toString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeOrderUiState.date.toString())
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                fakeOrderUiState.price
            )
        ).assertIsDisplayed()
    }

    @Test
    fun startOrderScreen_verifyContent() {
        val quantityOptions = listOf(
            Pair(R.string.one_cupcake, 1),
            Pair(R.string.six_cupcakes, 6),
            Pair(R.string.twelve_cupcakes, 12)
        )
        composeTestRule.setContent {
            StartOrderScreen(quantityOptions, {}
            )
        }
        quantityOptions.forEach { quantity ->
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(quantity.first))
                .assertIsDisplayed()
        }
    }

    @Test
    fun selectOptionScreen_verifyContent() {

        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        val subtotal = "$100"

        composeTestRule.setContent {
            SelectOptionScreen(
                subtotal = subtotal,
                options = flavors
            )
        }
        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()
    }

    @Test
    fun selectOptionScreen_optionSelected_NextButtonEnabled() {

        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        val subtotal = "$100"

        composeTestRule.setContent {
            SelectOptionScreen(
                subtotal = subtotal,
                options = flavors
            )
        }
        composeTestRule.onNodeWithText(flavors.asSequence().shuffled().first())
            .performClick()

        composeTestRule.onNodeWithStringId(R.string.next)
            .assertIsEnabled()
    }
}