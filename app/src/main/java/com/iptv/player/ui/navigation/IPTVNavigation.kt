package com.iptv.player.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iptv.player.ui.epg.EpgScreen
import com.iptv.player.ui.favorites.FavoritesScreen
import com.iptv.player.ui.home.HomeScreen
import com.iptv.player.ui.player.PlayerScreen
import com.iptv.player.ui.settings.SettingsScreen
import com.iptv.player.ui.theme.Primary

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    data object Home : Screen("home", "频道", Icons.Default.Home)
    data object Favorites : Screen("favorites", "收藏", Icons.Default.Favorite)
    data object Epg : Screen("epg", "节目指南", Icons.Default.Tv)
    data object Settings : Screen("settings", "设置", Icons.Default.Settings)
    data object Player : Screen("player/{channelId}", "播放器") {
        fun createRoute(channelId: Long) = "player/$channelId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Favorites,
    Screen.Epg,
    Screen.Settings
)

@Composable
fun MaxiTVNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 判断是否显示底部导航栏
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon!!,
                                    contentDescription = screen.title
                                )
                            },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                indicatorColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onChannelClick = { channelId ->
                        navController.navigate(Screen.Player.createRoute(channelId))
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onChannelClick = { channelId ->
                        navController.navigate(Screen.Player.createRoute(channelId))
                    }
                )
            }

            composable(Screen.Epg.route) {
                EpgScreen(
                    onChannelClick = { channelId ->
                        navController.navigate(Screen.Player.createRoute(channelId))
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }

            composable(
                route = Screen.Player.route,
                arguments = listOf(
                    navArgument("channelId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val channelId = backStackEntry.arguments?.getLong("channelId") ?: 0L
                PlayerScreen(
                    channelId = channelId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
