package com.unwur.weighbridge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unwur.weighbridge.AppScreen.DETAIL_SCREEN
import com.unwur.weighbridge.AppScreen.EDIT_SCREEN
import com.unwur.weighbridge.AppScreen.LIST_SCREEN
import com.unwur.weighbridge.AppScreen.NEW_TICKET_SCREEN
import com.unwur.weighbridge.DestinationArgs.TICKET_KEY
import com.unwur.weighbridge.addticket.AddTicketScreen
import com.unwur.weighbridge.detail.DetailScreen
import com.unwur.weighbridge.editticket.EditScreen
import com.unwur.weighbridge.tickets.ListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LIST_ROUTE,
        modifier = modifier
    ) {
        composable(Destinations.LIST_ROUTE) {
            ListScreen(
                onAddNewTicket = {
                    navController.navigate(Destinations.NEW_TICKET_ROUTE)
                },
                onOpenDetail = { ticketKey ->
                    navController.navigate(
                        "detail?ticketKey=$ticketKey"
                    )
                },
                onEditTicket = { ticketKey ->
                    navController.navigate("edit?ticketKey=$ticketKey")
                }
            )
        }
        composable(
            route = Destinations.DETAIL_ROUTE,
            arguments = listOf(
                navArgument(TICKET_KEY) {
                    type = NavType.StringType
                }
            )
        ) {
            val args = it.arguments
            DetailScreen(
                ticketKey = args?.getString(TICKET_KEY).orEmpty(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Destinations.EDIT_ROUTE,
            arguments = listOf(
                navArgument(TICKET_KEY) {
                    type = NavType.StringType
                }
            )
        ) {
            val args = it.arguments
            EditScreen(
                ticketKey = args?.getString(TICKET_KEY).orEmpty(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Destinations.NEW_TICKET_ROUTE
        ) {
            AddTicketScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

private object AppScreen {
    const val LIST_SCREEN = "list"
    const val DETAIL_SCREEN = "detail"
    const val EDIT_SCREEN = "edit"
    const val NEW_TICKET_SCREEN = "newTicket"
}

object DestinationArgs {
    const val TICKET_KEY = "ticketKey"
}

object Destinations {
    const val LIST_ROUTE = LIST_SCREEN
    const val DETAIL_ROUTE = "$DETAIL_SCREEN?$TICKET_KEY={$TICKET_KEY}"
    const val EDIT_ROUTE = "$EDIT_SCREEN?$TICKET_KEY={$TICKET_KEY}"
    const val NEW_TICKET_ROUTE = NEW_TICKET_SCREEN
}
