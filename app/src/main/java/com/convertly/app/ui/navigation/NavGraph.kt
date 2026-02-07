package com.convertly.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.convertly.app.billing.BillingManager
import com.convertly.app.data.model.UnitCategory
import com.convertly.app.ui.screens.*

object Routes {
    const val HOME = "home"
    const val CONVERTER = "converter/{category}"
    const val SETTINGS = "settings"
    const val HISTORY = "history"
    const val FAVORITES = "favorites"

    fun converter(category: UnitCategory) = "converter/${category.name}"
}

@Composable
fun ConvertlyNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
    billingManager: BillingManager,
    showAds: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                showAds = showAds,
                onCategorySelected = { category ->
                    navController.navigate(Routes.converter(category))
                },
                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                },
                onHistoryClick = {
                    navController.navigate(Routes.HISTORY)
                },
                onFavoritesClick = {
                    navController.navigate(Routes.FAVORITES)
                }
            )
        }

        composable(
            route = Routes.CONVERTER,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category") ?: UnitCategory.LENGTH.name
            val category = try {
                UnitCategory.valueOf(categoryName)
            } catch (_: Exception) {
                UnitCategory.LENGTH
            }

            ConverterScreen(
                viewModel = viewModel,
                category = category,
                showAds = showAds,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = viewModel,
                billingManager = billingManager,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFavoriteClick = { fav ->
                    viewModel.loadFavoriteConversion(fav)
                    navController.navigate(Routes.converter(fav.category)) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }
    }
}
